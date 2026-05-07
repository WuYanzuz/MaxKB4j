package com.asiainfo.model.custom.disabled;

import com.asiainfo.model.service.STTModel;
import dev.langchain4j.model.ModelDisabledException;

public class DisabledSTTModel implements STTModel {
    @Override
    public String speechToText(byte[] audioBytes, String suffix) {
        throw new ModelDisabledException("STTModel is disabled");
    }
}
