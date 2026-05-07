package com.asiainfo.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.asiainfo.knowledge.entity.ProblemEntity;
import com.asiainfo.knowledge.entity.ProblemParagraphEntity;
import com.asiainfo.knowledge.vo.ProblemParagraphVO;

import java.util.List;

/**
 * @author tarzan
 * @date 2024-12-27 11:23:44
 */
public interface ProblemParagraphMapper extends BaseMapper<ProblemParagraphEntity>{

    List<ProblemParagraphVO> getProblems(String knowledgeId, List<String>  docIds);

    List<ProblemEntity> getProblemsByParagraphId(String paragraphId);
}
