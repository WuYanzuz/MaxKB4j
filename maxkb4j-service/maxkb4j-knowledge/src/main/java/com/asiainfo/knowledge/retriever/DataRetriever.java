package com.asiainfo.knowledge.retriever;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.asiainfo.knowledge.consts.SearchType;
import com.asiainfo.knowledge.entity.DocumentEntity;
import com.asiainfo.knowledge.retrieval.SearchMode;
import com.asiainfo.knowledge.retrieval.SearchRequest;
import com.asiainfo.knowledge.retrieval.IDataRetriever;
import com.asiainfo.knowledge.service.IDocumentService;
import com.asiainfo.knowledge.store.IDataStore;
import com.asiainfo.knowledge.store.VectorStoreImpl;
import com.asiainfo.knowledge.store.FullTextStoreImpl;
import com.asiainfo.knowledge.store.CompositeStoreImpl;
import com.asiainfo.knowledge.vo.TextChunkVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Unified data retriever that supports multiple search modes
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataRetriever implements IDataRetriever {

    private final VectorStoreImpl vectorStore;
    private final FullTextStoreImpl fullTextStore;
    private final CompositeStoreImpl compositeStore;
    private final IDocumentService documentService;

    private static final Map<String, SearchMode> SEARCH_MODE_MAP = Map.of(
        SearchType.EMBEDDING, SearchMode.VECTOR,
        SearchType.FULL_TEXT, SearchMode.FULL_TEXT,
        SearchType.HYBRID, SearchMode.HYBRID
    );

    @Override
    public List<TextChunkVO> search(List<String> knowledgeIds, List<String> excludeParagraphIds,
                                     String keyword, int maxResults, float minScore, String searchMode) {
        SearchRequest request = new SearchRequest();
        request.setKnowledgeIds(knowledgeIds);
        request.setExcludeParagraphIds(excludeParagraphIds);
        request.setQuery(keyword);
        request.setTopK(maxResults);
        request.setMinScore(minScore);
        request.setMode(SEARCH_MODE_MAP.get(searchMode));
        List<DocumentEntity> excludeDocuments =documentService.lambdaQuery().select(DocumentEntity::getId).in(DocumentEntity::getKnowledgeId, knowledgeIds).eq(DocumentEntity::getIsActive, false).list();
        if (CollectionUtils.isNotEmpty(excludeDocuments)){
            request.setExcludeDocumentIds(excludeDocuments.stream().map(DocumentEntity::getId).toList());
        }
        return getStore(searchMode).search(request);
    }

    private IDataStore getStore(String searchMode) {
        return switch (searchMode) {
            case SearchType.EMBEDDING -> vectorStore;
            case SearchType.FULL_TEXT -> fullTextStore;
            case SearchType.HYBRID -> compositeStore;
            default -> throw new IllegalArgumentException("Unknown search mode: " + searchMode);
        };
    }
}