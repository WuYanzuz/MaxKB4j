package com.maxkb4j.knowledge.task;

import com.alibaba.fastjson.JSONObject;
import com.maxkb4j.knowledge.config.DocumentWatchProperties;
import com.maxkb4j.knowledge.consts.KnowledgeType;
import com.maxkb4j.knowledge.dto.DocumentSimple;
import com.maxkb4j.knowledge.dto.ParagraphSimple;
import com.maxkb4j.knowledge.entity.DocumentEntity;
import com.maxkb4j.knowledge.service.DocumentParseService;
import com.maxkb4j.knowledge.service.DocumentService;
import com.maxkb4j.knowledge.service.DocumentSplitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * 文档目录增量监控定时任务
 * 定期扫描配置的目录，检测文件新增/修改/删除，并同步到对应知识库
 *
 * 安全隔离：只操作 meta.source="watch" 的文档，不影响页面手动上传的文档
 */
@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "maxkb4j.document-watch", name = "enabled", havingValue = "true")
public class DocumentWatchTask {

    /** 监控任务导入的文档将在 meta 中打上此标记，与页面上传的文档区分 */
    private static final String WATCH_SOURCE_TAG = "watch";

    /** meta 中记录文件最后修改时间的键名 */
    private static final String META_LAST_MODIFIED = "lastModified";

    private final DocumentWatchProperties properties;
    private final DocumentService documentService;
    private final DocumentParseService documentParseService;
    private final DocumentSplitService documentSplitService;

    /**
     * 每隔 interval-seconds 秒执行一次扫描（默认60秒，满足5分钟内生效要求）
     * fixedDelay 从上次执行结束后开始计时，避免并发执行
     */
    @Scheduled(fixedDelayString = "${maxkb4j.document-watch.interval-seconds:60}000")
    public void scan() {
        List<DocumentWatchProperties.WatchDirectory> directories = properties.getDirectories();
        if (directories == null || directories.isEmpty()) {
            return;
        }
        for (DocumentWatchProperties.WatchDirectory dir : directories) {
            try {
                scanDirectory(dir);
            } catch (Exception e) {
                log.error("[文档监控] 扫描目录异常: path={}, knowledgeId={}", dir.getPath(), dir.getKnowledgeId(), e);
            }
        }
    }

    private void scanDirectory(DocumentWatchProperties.WatchDirectory dir) throws IOException {
        String knowledgeId = dir.getKnowledgeId();
        Path dirPath = Paths.get(dir.getPath());

        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            log.warn("[文档监控] 目录不存在或不是目录: {}", dir.getPath());
            return;
        }

        // 当前目录下所有支持的文件（key = 相对路径）
        Map<String, Path> currentFiles = new LinkedHashMap<>();
        Files.walkFileTree(dirPath, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String fileName = file.getFileName().toString();
                if (isSupportedFile(fileName)) {
                    String relPath = dirPath.relativize(file).toString();
                    currentFiles.put(relPath, file);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        // 只获取带有 watch 标记的文档，不影响页面手动上传的文档
        List<DocumentEntity> allDocs = documentService.listDocByKnowledgeId(knowledgeId);
        Map<String, DocumentEntity> watchDocMap = new LinkedHashMap<>();
        for (DocumentEntity doc : allDocs) {
            if (isWatchSourceDoc(doc)) {
                watchDocMap.put(doc.getName(), doc);
            }
        }

        // 1. 检测新增 & 修改
        for (Map.Entry<String, Path> entry : currentFiles.entrySet()) {
            String relPath = entry.getKey();
            Path filePath = entry.getValue();
            long lastModified = Files.getLastModifiedTime(filePath).toMillis();

            DocumentEntity existingDoc = watchDocMap.get(relPath);
            if (existingDoc == null) {
                // 新增文件
                log.info("[文档监控] 检测到新增文件: {}", relPath);
                importFile(knowledgeId, relPath, filePath, lastModified);
            } else {
                // 已有文件，通过 meta 中的 lastModified 判断是否修改过
                Long dbLastModified = getDocLastModified(existingDoc);
                if (dbLastModified == null) {
                    // 老数据没有记录修改时间，补录当前时间，当作已同步
                    log.info("[文档监控] 补录文件修改时间: {}", relPath);
                    updateDocLastModified(existingDoc, lastModified);
                } else if (lastModified > dbLastModified) {
                    // 文件已修改（修改时间比数据库记录的新）
                    log.info("[文档监控] 检测到文件修改: {}，旧时间={}, 新时间={}", relPath, dbLastModified, lastModified);
                    documentService.deleteDocByIds(knowledgeId, List.of(existingDoc.getId()));
                    importFile(knowledgeId, relPath, filePath, lastModified);
                }
            }
        }

        // 2. 检测删除（watch文档有但目录中不存在的文件）
        for (Map.Entry<String, DocumentEntity> entry : watchDocMap.entrySet()) {
            String relPath = entry.getKey();
            if (!currentFiles.containsKey(relPath)) {
                log.info("[文档监控] 检测到文件删除: {}", relPath);
                documentService.deleteDocByIds(knowledgeId, List.of(entry.getValue().getId()));
            }
        }
    }

    /**
     * 解析文件并导入知识库，同时写入 source=watch 标记和文件修改时间
     */
    private void importFile(String knowledgeId, String relPath, Path filePath, long lastModified) {
        String fileName = filePath.getFileName().toString();
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            String text = documentParseService.extractText(fileName, new ByteArrayInputStream(bytes));
            if (text == null || text.isBlank()) {
                log.warn("[文档监控] 文件内容为空，跳过导入: {}", relPath);
                return;
            }
            List<ParagraphSimple> segments = documentSplitService.smartSplit(text);
            DocumentSimple doc = new DocumentSimple(relPath, text);
            // 写入 source=watch 标记 + 文件修改时间，与页面上传的文档区分
            JSONObject meta = new JSONObject();
            meta.put("source", WATCH_SOURCE_TAG);
            meta.put("watchPath", filePath.toAbsolutePath().toString());
            meta.put(META_LAST_MODIFIED, lastModified);
            doc.setMeta(meta);
            doc.getParagraphs().addAll(segments);
            documentService.batchCreateDocs(knowledgeId, KnowledgeType.BASE, List.of(doc));
            log.info("[文档监控] 文件导入成功: {}, 分段数: {}", relPath, segments.size());
        } catch (IOException e) {
            log.error("[文档监控] 文件读取失败: {}", relPath, e);
        } catch (Exception e) {
            log.error("[文档监控] 文件导入异常: {}", relPath, e);
        }
    }

    /**
     * 判断该文档是否是由监控任务导入的（通过 meta.source 标记识别）
     */
    private boolean isWatchSourceDoc(DocumentEntity doc) {
        if (doc.getMeta() == null) return false;
        return WATCH_SOURCE_TAG.equals(doc.getMeta().getString("source"));
    }

    /**
     * 从文档 meta 中读取文件最后修改时间
     */
    private Long getDocLastModified(DocumentEntity doc) {
        if (doc.getMeta() == null) return null;
        return doc.getMeta().getLong(META_LAST_MODIFIED);
    }

    /**
     * 更新文档 meta 中的文件最后修改时间
     */
    private void updateDocLastModified(DocumentEntity doc, long lastModified) {
        JSONObject meta = doc.getMeta();
        if (meta == null) {
            meta = new JSONObject();
        }
        meta.put(META_LAST_MODIFIED, lastModified);
        documentService.lambdaUpdate()
                .eq(DocumentEntity::getId, doc.getId())
                .set(DocumentEntity::getMeta, meta)
                .update();
    }

    /**
     * 判断是否为支持的文档格式
     */
    private boolean isSupportedFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".pdf")
                || lower.endsWith(".docx")
                || lower.endsWith(".doc")
                || lower.endsWith(".txt")
                || lower.endsWith(".md")
                || lower.endsWith(".xlsx")
                || lower.endsWith(".xls")
                || lower.endsWith(".csv");
    }
}

