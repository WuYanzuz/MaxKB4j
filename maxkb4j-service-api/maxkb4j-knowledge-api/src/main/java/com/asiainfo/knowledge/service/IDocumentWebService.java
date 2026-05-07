package com.asiainfo.knowledge.service;

import com.asiainfo.knowledge.dto.DocumentSimple;

import java.util.List;

public interface IDocumentWebService {
    List<DocumentSimple> getDocumentList(String sourceUrl, String selector, boolean isRecursive);
}
