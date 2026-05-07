package com.asiainfo.application.service;

import com.asiainfo.application.vo.ApplicationVO;
import com.asiainfo.common.domain.dto.ChatMessageVO;
import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.domain.dto.ChatResponse;
import reactor.core.publisher.Sinks;

public interface IChatService {

    ChatResponse chatMessage(ApplicationVO application, ChatParams chatParams, Sinks.Many<ChatMessageVO> sink);
}
