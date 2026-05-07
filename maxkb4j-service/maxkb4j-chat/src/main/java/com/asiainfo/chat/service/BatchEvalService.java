package com.asiainfo.chat.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.asiainfo.application.service.IApplicationChatService;
import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.domain.dto.ChatResponse;
import com.asiainfo.common.enums.ChatUserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 批量评测服务
 * 支持上传测试集（CSV/JSONL），逐条调用AI问答，输出结果文件供下载
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BatchEvalService {

    private final IApplicationChatService chatService;

    /**
     * 任务状态缓存: taskId -> 状态信息
     */
    private final ConcurrentHashMap<String, TaskInfo> taskCache = new ConcurrentHashMap<>();

    /**
     * 提交批量评测任务（异步执行）
     *
     * @param appId     应用ID
     * @param questions 问题列表（从上传文件解析）
     * @return taskId
     */
    public String submitTask(String appId, List<String> questions) {
        String taskId = IdWorker.get32UUID();
        TaskInfo info = new TaskInfo(taskId, questions.size());
        taskCache.put(taskId, info);
        runBatchAsync(appId, questions, info);
        return taskId;
    }

    /**
     * 查询任务状态
     */
    public TaskInfo getTaskInfo(String taskId) {
        return taskCache.get(taskId);
    }

    /**
     * 获取结果文件字节流（任务完成后可调用）
     */
    public byte[] getResultBytes(String taskId) {
        TaskInfo info = taskCache.get(taskId);
        if (info == null) {
            throw new IllegalArgumentException("任务不存在: " + taskId);
        }
        if (!info.isDone()) {
            throw new IllegalStateException("任务尚未完成，当前进度: " + info.getDone() + "/" + info.getTotal());
        }
        return info.getResultCsvBytes();
    }

    @Async
    public void runBatchAsync(String appId, List<String> questions, TaskInfo info) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"序号", "问题", "回答", "状态"});

        for (int i = 0; i < questions.size(); i++) {
            String question = questions.get(i).trim();
            if (question.isEmpty()) {
                info.incrementDone();
                continue;
            }
            String answer;
            String status;
            try {
                String chatId = chatService.chatOpen(appId, false);
                ChatParams params = ChatParams.builder()
                        .message(question)
                        .reChat(false)
                        .stream(false)
                        .chatId(chatId)
                        .appId(appId)
                        .chatUserId(IdWorker.get32UUID())
                        .chatUserType(ChatUserType.ANONYMOUS_USER.name())
                        .debug(false)
                        .build();
                ChatResponse response = chatService.chatMessage(params, Sinks.many().unicast().onBackpressureBuffer());
                answer = response.getAnswer();
                status = "成功";
            } catch (Exception e) {
                log.warn("批量评测第{}条失败: {}", i + 1, e.getMessage());
                answer = "评测失败: " + e.getMessage();
                status = "失败";
            }
            rows.add(new String[]{String.valueOf(i + 1), question, answer, status});
            info.incrementDone();
        }

        // 写入CSV字节
        info.setResultCsvBytes(writeCsv(rows));
        info.setDone(true);
    }

    /**
     * 将二维数组写成 CSV 字节（UTF-8 BOM，Excel可直接打开）
     */
    private byte[] writeCsv(List<String[]> rows) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // UTF-8 BOM
        baos.writeBytes(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(baos, StandardCharsets.UTF_8))) {
            for (String[] row : rows) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) sb.append(',');
                    sb.append(escapeCsv(row[i]));
                }
                pw.println(sb);
            }
        }
        return baos.toByteArray();
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * 解析上传的文件内容，提取问题列表
     * 支持格式：
     * - JSON 数组：[{"question":"..."}, ...]  或  ["问题1", "问题2"]
     * - CSV（单列问题，或首行header为"question"/"问题"的多列）
     * - TXT（每行一个问题）
     */
    public List<String> parseQuestions(byte[] fileBytes, String filename) throws IOException {
        String content = new String(fileBytes, StandardCharsets.UTF_8)
                // 去除 BOM
                .replaceFirst("^\uFEFF", "").trim();
        List<String> questions = new ArrayList<>();
        String lower = filename.toLowerCase();

        if (lower.endsWith(".json") || lower.endsWith(".jsonl")) {
            // 优先尝试 JSON 数组格式
            if (content.startsWith("[")) {
                try {
                    com.alibaba.fastjson.JSONArray array = com.alibaba.fastjson.JSONArray.parseArray(content);
                    for (int i = 0; i < array.size(); i++) {
                        Object item = array.get(i);
                        if (item instanceof com.alibaba.fastjson.JSONObject obj) {
                            // 对象格式：{"question":"...", ...}
                            String q = obj.getString("question");
                            if (q == null) q = obj.getString("q");
                            if (q == null) q = obj.getString("input");
                            if (q != null && !q.isBlank()) questions.add(q);
                        } else if (item instanceof String s && !s.isBlank()) {
                            // 字符串格式：["问题1", "问题2"]
                            questions.add(s);
                        }
                    }
                    return questions;
                } catch (Exception e) {
                    log.debug("JSON数组解析失败，尝试逐行解析: {}", e.getMessage());
                }
            }
            // 兼容 JSONL：每行一个 JSON 对象
            for (String line : content.split("\\r?\\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject(line);
                    String q = obj.getString("question");
                    if (q == null) q = obj.getString("q");
                    if (q == null) q = obj.getString("input");
                    if (q != null && !q.isBlank()) questions.add(q);
                } catch (Exception e) {
                    log.debug("跳过无效JSON行: {}", line);
                }
            }
        } else {
            // CSV / TXT: 逐行解析
            String[] lines = content.split("\\r?\\n");
            boolean firstLine = true;
            int questionCol = 0;
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] cols = splitCsvLine(line);
                if (firstLine) {
                    firstLine = false;
                    // 判断首行是否是 header
                    for (int i = 0; i < cols.length; i++) {
                        String h = cols[i].trim().toLowerCase();
                        if (h.equals("question") || h.equals("问题") || h.equals("q")) {
                            questionCol = i;
                            break;
                        }
                    }
                    // 如果首行就像是 header（非纯问题内容），跳过
                    String firstCell = cols[questionCol].trim().toLowerCase();
                    if (firstCell.equals("question") || firstCell.equals("问题") || firstCell.equals("q")) {
                        continue;
                    }
                }
                if (cols.length > questionCol) {
                    String q = cols[questionCol].trim();
                    if (!q.isEmpty()) questions.add(q);
                }
            }
        }
        return questions;
    }

    private String[] splitCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuote = false;
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuote && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuote = !inQuote;
                }
            } else if (c == ',' && !inQuote) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }

    /**
     * 任务状态信息
     */
    public static class TaskInfo {
        private final String taskId;
        private final int total;
        private volatile int done;
        private volatile boolean finished;
        private volatile byte[] resultCsvBytes;

        public TaskInfo(String taskId, int total) {
            this.taskId = taskId;
            this.total = total;
            this.done = 0;
            this.finished = false;
        }

        public synchronized void incrementDone() { done++; }
        public String getTaskId() { return taskId; }
        public int getTotal() { return total; }
        public int getDone() { return done; }
        public boolean isDone() { return finished; }
        public void setDone(boolean done) { this.finished = done; }
        public byte[] getResultCsvBytes() { return resultCsvBytes; }
        public void setResultCsvBytes(byte[] resultCsvBytes) { this.resultCsvBytes = resultCsvBytes; }
    }
}
