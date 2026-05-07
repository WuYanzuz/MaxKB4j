package com.asiainfo.application.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.application.dto.EmbedDTO;
import com.asiainfo.application.entity.ApplicationEntity;
import com.asiainfo.application.vo.ApplicationVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IApplicationService extends IService<ApplicationEntity> {

    String speechToText(String appId, MultipartFile file, boolean debug) throws IOException;
    byte[] textToSpeech(String appId, JSONObject data, boolean debug);
    String embed(EmbedDTO dto);
    ApplicationVO appProfile(String appId);
}
