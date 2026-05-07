package com.asiainfo.application.controller;

import com.asiainfo.application.service.ApplicationChatService;
import com.asiainfo.common.api.R;
import com.asiainfo.common.constant.AppConst;
import com.asiainfo.common.domain.dto.ChatMessageVO;
import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.enums.ChatSource;
import com.asiainfo.common.enums.ChatUserType;
import com.asiainfo.common.util.StpKit;
import com.asiainfo.common.util.WebUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;


/**
 * @author tarzan
 * @date 2024-12-25 13:09:54
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConst.ADMIN_API)
public class ChatMessageController {

    private final ApplicationChatService chatService;

    @GetMapping("/workspace/default/application/{id}/open")
    public R<String> open(@PathVariable("id") String id) {
        return R.success(chatService.chatOpen(id, true));
    }

    @PostMapping(path = "/chat_message/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatMessageVO> chatMessage(@PathVariable String chatId, @RequestBody ChatParams params) {
        Sinks.Many<ChatMessageVO> sink = Sinks.many().unicast().onBackpressureBuffer();
        params.setChatId(chatId);
        params.setChatUserId(StpKit.ADMIN.getLoginIdAsString());
        params.setChatUserType(ChatUserType.ANONYMOUS_USER.name());
        params.setSource(ChatSource.ONLINE);
        params.setIpAddress(WebUtil.getIP());
        params.setDebug(true);
        // 异步执行业务逻辑
        chatService.chatMessageAsync(params, sink);
        return sink.asFlux();
    }
}
