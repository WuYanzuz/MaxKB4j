package com.asiainfo.model.provider;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.common.domain.form.BaseField;
import com.asiainfo.common.domain.form.TextInputField;
import com.asiainfo.common.mp.entity.ModelCredential;
import com.asiainfo.model.custom.credential.ModelCredentialForm;
import com.asiainfo.model.custom.model.BaiLianImageModel;
import com.asiainfo.model.custom.model.BaiLianReranker;
import com.asiainfo.model.custom.model.BaiLianSTTModel;
import com.asiainfo.model.custom.model.BaiLianTTSModel;
import com.asiainfo.model.custom.params.impl.*;
import com.asiainfo.model.enums.ModelType;
import com.asiainfo.model.service.STTModel;
import com.asiainfo.model.service.TTSModel;
import com.asiainfo.model.vo.ModelInfo;
import dev.langchain4j.community.model.dashscope.*;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.scoring.ScoringModel;

import java.util.ArrayList;
import java.util.List;

/**
 * AliYun BaiLian (DashScope) Model Provider
 */
public class AliYunBaiLianModelProvider extends AbsModelProvider {

    private static final List<ModelInfo> MODEL_INFOS = List.of(
            new ModelInfo(QwenModelName.QWEN_TURBO, "", ModelType.LLM),
            new ModelInfo("qwen3.5-plus", "", ModelType.LLM, new QWenChatModelParams(true)),
            new ModelInfo(QwenModelName.QWEN_PLUS, "", ModelType.LLM),
            new ModelInfo(QwenModelName.QWEN_MAX, "", ModelType.LLM),
            new ModelInfo("text-embedding-v3", "", ModelType.EMBEDDING),
            new ModelInfo("text-embedding-v4", "", ModelType.EMBEDDING),
            new ModelInfo("text-embedding-v3", "", ModelType.EMBEDDING),
            new ModelInfo("paraformer-realtime-v2", "", ModelType.STT),
            new ModelInfo("fun-asr-realtime", "", ModelType.STT),
            new ModelInfo("gummy-realtime-v1", "", ModelType.STT, new GummySTTParams()),
            new ModelInfo("cosyvoice-v1", "", ModelType.TTS, new CosyVoiceV1TTSParams()),
            new ModelInfo("cosyvoice-v2", "", ModelType.TTS, new CosyVoiceV2TTSParams()),
            new ModelInfo("sambert-v1", "", ModelType.TTS, new SamBertTTSParams()),
            new ModelInfo("qwen3-tts-flash", "", ModelType.TTS, new QWenTTSParams()),
            new ModelInfo("qwen-tts", "", ModelType.TTS, new QWenTTSParams()),
            new ModelInfo("qwen3.5-plus", "", ModelType.VISION, new QWenChatModelParams(true)),
            new ModelInfo(QwenModelName.QWEN_VL_PLUS, "", ModelType.VISION, new QWenChatModelParams(true)),
            new ModelInfo(QwenModelName.QWEN_VL_MAX, "", ModelType.VISION, new QWenChatModelParams(true)),
            new ModelInfo(WanxModelName.WANX2_1_T2I_TURBO, "", ModelType.TTI, new WanXImageModelParams()),
            new ModelInfo(WanxModelName.WANX2_1_T2I_PLUS, "", ModelType.TTI, new WanXImageModelParams()),
            new ModelInfo("qwen-image-plus", "", ModelType.TTI, new QwenImageModelParams()),
            new ModelInfo("gte-rerank", "", ModelType.RERANKER),
            new ModelInfo("qwen3-rerank", "", ModelType.RERANKER)
    );

    @Override
    public List<BaseField> getChatModelParamsForm() {
        return new QWenChatModelParams().toForm();
    }

    @Override
    public ModelCredentialForm getModelCredential() {
        // 显示 baseUrl 输入框，但非必填（有默认值）
        return new ModelCredentialForm(true, true) {
            @Override
            public List<BaseField> toForm() {
                List<BaseField> list = new ArrayList<>(2);
                // baseUrl 非必填，有默认提示
                list.add(new TextInputField("API 域名（可选）", "baseUrl", false, "https://dashscope.aliyuncs.com/compatible-mode/v1"));
                list.add(new TextInputField("API KEY", "apiKey", true));
                return list;
            }
        };
    }

    @Override
    public List<ModelInfo> getModelList() {
        return MODEL_INFOS;
    }

    @Override
    public ChatModel buildChatModel(String modelName, ModelCredential credential, JSONObject params) {
        var builder = QwenChatModel.builder()
                .apiKey(credential.getApiKey())
                .modelName(modelName)
                .temperature(getFloatParam(params, "temperature"))
                .maxTokens(getIntParam(params, "maxTokens"))
                .isMultimodalModel(getBooleanParam(params, "isMultimodalModel"));
        if (credential.getBaseUrl() != null && !credential.getBaseUrl().isEmpty()) {
            builder.baseUrl(credential.getBaseUrl());
        }
        return builder.build();
    }

    @Override
    public StreamingChatModel buildStreamingChatModel(String modelName, ModelCredential credential, JSONObject params) {
        var builder = QwenStreamingChatModel.builder()
                .apiKey(credential.getApiKey())
                .modelName(modelName)
                .temperature(getFloatParam(params, "temperature"))
                .maxTokens(getIntParam(params, "maxTokens"))
                .isMultimodalModel(getBooleanParam(params, "isMultimodalModel"));
        if (credential.getBaseUrl() != null && !credential.getBaseUrl().isEmpty()) {
            builder.baseUrl(credential.getBaseUrl());
        }
        return builder.build();
    }

    @Override
    public EmbeddingModel buildEmbeddingModel(String modelName, ModelCredential credential, JSONObject params) {
        var builder = QwenEmbeddingModel.builder()
                .apiKey(credential.getApiKey())
                .modelName(modelName)
                .dimension(getIntParam(params, "dimension"));
        if (credential.getBaseUrl() != null && !credential.getBaseUrl().isEmpty()) {
            builder.baseUrl(credential.getBaseUrl());
        }
        return builder.build();
    }

    @Override
    public ImageModel buildImageModel(String modelName, ModelCredential credential, JSONObject params) {
        return new BaiLianImageModel(modelName, credential, params);
    }

    @Override
    public ScoringModel buildScoringModel(String modelName, ModelCredential credential, JSONObject params) {
        return new BaiLianReranker(modelName, credential, params);
    }

    @Override
    public STTModel buildSTTModel(String modelName, ModelCredential credential, JSONObject params) {
        return new BaiLianSTTModel(modelName, credential, params);
    }

    @Override
    public TTSModel buildTTSModel(String modelName, ModelCredential credential, JSONObject params) {
        return new BaiLianTTSModel(modelName, credential, params);
    }
}
