package com.asiainfo.application.handler;


import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.domain.dto.ChatResponse;

public interface PostResponseHandler {

    void handler(ChatParams chatParams, ChatResponse chatResponse, long startTime);
}
