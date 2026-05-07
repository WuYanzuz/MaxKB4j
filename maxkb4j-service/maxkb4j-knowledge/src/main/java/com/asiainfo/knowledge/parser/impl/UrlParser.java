package com.asiainfo.knowledge.parser.impl;

import com.asiainfo.common.util.IoUtil;
import com.asiainfo.knowledge.parser.DocumentParser;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Component
public class UrlParser implements DocumentParser {

    private final FlexmarkHtmlConverter converter = FlexmarkHtmlConverter.builder().build();

    @Override
    public List<String> getExtensions() {
        return List.of(".url");
    }

    @Override
    public String handle(InputStream inputStream) {
        return converter.convert(IoUtil.readToString(inputStream));
    }
}
