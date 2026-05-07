package com.asiainfo.model.provider;

import com.asiainfo.model.custom.credential.ModelCredentialForm;
import com.asiainfo.model.vo.ModelInfo;

import java.util.List;

/**
 * Local Model Provider - ONNX local models
 */
public class LocalModelProvider extends AbsModelProvider {

    private static final List<ModelInfo> MODEL_INFOS = List.of();


    @Override
    public List<ModelInfo> getModelList() {
        return MODEL_INFOS;
    }

    @Override
    public ModelCredentialForm getModelCredential() {
        return new ModelCredentialForm(false, false);
    }

}
