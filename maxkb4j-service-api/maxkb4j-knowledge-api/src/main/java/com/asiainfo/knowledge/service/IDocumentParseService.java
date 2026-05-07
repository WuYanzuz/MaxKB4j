package com.asiainfo.knowledge.service;

import java.io.InputStream;

public interface IDocumentParseService {

    String extractText(String fileName, InputStream inputStream);
}
