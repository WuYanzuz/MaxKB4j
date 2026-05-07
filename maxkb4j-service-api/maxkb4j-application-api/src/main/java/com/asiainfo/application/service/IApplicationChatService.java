package com.asiainfo.application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.asiainfo.application.dto.ShareChatDTO;
import com.asiainfo.application.entity.ApplicationChatEntity;
import com.asiainfo.application.vo.ShareChatVO;
import com.asiainfo.common.domain.dto.ChatMessageVO;
import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.domain.dto.ChatResponse;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IApplicationChatService extends IService<ApplicationChatEntity> {

    String chatOpen(String appId, boolean debug);

    ChatResponse chatMessage(ChatParams chatParams, Sinks.Many<ChatMessageVO> sink);
    CompletableFuture<ChatResponse> chatMessageAsync(ChatParams chatParams, Sinks.Many<ChatMessageVO> sink);
    Boolean deleteById(String chatId);

    Map<String, String> shareChat(String id, String chatId, ShareChatDTO dto);

    ShareChatVO shareChat(String id);
}
