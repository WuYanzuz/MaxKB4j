package com.asiainfo.knowledge.service;

import com.asiainfo.common.mp.entity.KnowledgeSetting;
import com.asiainfo.knowledge.vo.ParagraphVO;

import java.util.List;

public interface IRetrieveService {
    List<ParagraphVO> paragraphSearch(String question, List<String> knowledgeIds, List<String> excludeParagraphIds, KnowledgeSetting datasetSetting);
}
