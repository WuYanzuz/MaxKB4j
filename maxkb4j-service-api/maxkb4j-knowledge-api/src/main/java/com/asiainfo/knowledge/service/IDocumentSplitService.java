package com.asiainfo.knowledge.service;

import com.asiainfo.knowledge.dto.ParagraphSimple;

import java.util.List;

public interface IDocumentSplitService {

    List<ParagraphSimple> split(String docText, String[] patterns, Integer limit, Boolean withFilter);
}
