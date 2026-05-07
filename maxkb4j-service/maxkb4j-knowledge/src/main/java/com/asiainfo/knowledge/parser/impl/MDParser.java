package com.asiainfo.knowledge.parser.impl;

import com.asiainfo.common.util.IoUtil;
import com.asiainfo.knowledge.parser.DocumentParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MDParser implements DocumentParser {

    @Override
    public List<String> getExtensions() {
        return List.of(".md");
    }

    @Override
    public String handle(InputStream inputStream) {
        return IoUtil.readToString(inputStream);
    }
}
