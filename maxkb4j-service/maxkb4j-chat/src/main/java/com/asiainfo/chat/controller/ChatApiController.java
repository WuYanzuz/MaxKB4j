package com.asiainfo.chat.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.asiainfo.application.dto.EmbedDTO;
import com.asiainfo.application.dto.ShareChatDTO;
import com.asiainfo.application.entity.*;
import com.asiainfo.application.service.*;
import com.asiainfo.application.vo.ApplicationChatRecordVO;
import com.asiainfo.application.vo.ShareChatVO;
import com.asiainfo.chat.service.BatchEvalService;
import com.asiainfo.chat.service.ChatApiService;
import com.asiainfo.common.annotation.SaCheckPerm;
import com.asiainfo.common.api.R;
import com.asiainfo.common.constant.AppConst;
import com.asiainfo.common.domain.dto.ChatMessageVO;
import com.asiainfo.common.domain.dto.ChatParams;
import com.asiainfo.common.domain.dto.ChatResponse;
import com.asiainfo.common.domain.dto.McpRequest;
import com.asiainfo.common.enums.ChatSource;
import com.asiainfo.common.enums.ChatUserType;
import com.asiainfo.common.enums.PermissionEnum;
import com.asiainfo.common.exception.ApiException;
import com.asiainfo.common.util.StpKit;
import com.asiainfo.common.util.WebUtil;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "MaxKB4J开放接口")
@RestController
@RequestMapping(AppConst.CHAT_API)
@RequiredArgsConstructor
public class ChatApiController {

    private final IApplicationAccessTokenService accessTokenService;
    private final IApplicationService applicationService;
    private final IApplicationChatService chatService;
    private final IApplicationChatRecordService chatRecordService;
    private final ChatApiService chatApiService;
    private final IApplicationApiKeyService apiKeyService;
    private final BatchEvalService batchEvalService;


    @Hidden
    @GetMapping("/profile")
    public R<JSONObject> profile(String accessToken) {
        ApplicationAccessTokenEntity appAccessToken = accessTokenService.getByAccessToken(accessToken);
        if (appAccessToken == null) {
            return R.fail("未找到应用");
        }
        JSONObject result = new JSONObject();
        result.put("authentication", appAccessToken.getAuthentication());
        return R.success(result);
    }


    @Hidden
    @PostMapping("/auth/anonymous")
    public R<String> auth(@RequestBody JSONObject params) {
        return R.success(chatApiService.authToken(params));
    }


    @Operation(summary = "获取应用相关信息", description = "获取应用相关信息")
    @GetMapping("/application/profile")
    public R<ApplicationEntity> appProfile() {
        if (StpKit.USER.isLogin()) {
            String appId = (String) StpKit.USER.getExtra("applicationId");
            return R.data(chatApiService.appProfile(appId));
        }
        return R.fail("未登录");
    }

    @Operation(summary = "获取应用的会话ID", description = "获取应用的会话ID(首次对话前，需要调用该接口，生成对话ID)")
    @GetMapping("/open")
    public R<String> chatOpen() {
        String appId = (String) StpKit.USER.getExtra("applicationId");
        return R.success(chatService.chatOpen(appId, false));
    }

    @Operation(summary = "聊天对话", description = "聊天对话")
    @PostMapping(path = "/chat_message/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    public Object chatMessage(@PathVariable String chatId, @RequestBody ChatParams params) {
        String userId = StpKit.USER.getLoginIdAsString();
        Sinks.Many<ChatMessageVO> sink = Sinks.many().unicast().onBackpressureBuffer();
        params.setChatId(chatId);
        params.setChatUserId(userId);
        params.setChatUserType(ChatUserType.ANONYMOUS_USER.name());
        params.setSource(ChatSource.ONLINE);
        params.setIpAddress(WebUtil.getIP());
        params.setDebug(false);
        if (Boolean.TRUE.equals(params.getStream())) {
            // 异步执行业务逻辑
            chatService.chatMessageAsync(params, sink);
            return sink.asFlux();
        } else {
            ChatResponse chatResponse = chatService.chatMessage(params, sink);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(R.data(chatResponse));
        }
    }

    @PostMapping(path = "/mcp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBodyEmitter handleMcpRequest(@RequestBody McpRequest req) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        String secretKey = WebUtil.getTokenValue();
        ApplicationApiKeyEntity apiKey = apiKeyService.getBySecretKey(secretKey);
        if (apiKey == null || !apiKey.getIsActive()) {
            emitter.completeWithError(new ApiException("token不合法或被禁用"));
        } else {
            // 异步处理（避免阻塞主线程）
            chatApiService.mcpHandleAsync(apiKey, req, emitter);
        }
        return emitter;
    }


    @Hidden
    @GetMapping("/historical_conversation/{current}/{size}")
    public R<Page<ApplicationChatEntity>> historicalConversation(@PathVariable int current, @PathVariable int size) {
        return R.data(chatApiService.historicalConversation(current, size));
    }

    @Hidden
    @GetMapping("/historical_conversation/{chatId}/record/{chatRecordId}")
    public R<ApplicationChatRecordVO> historicalConversation(@PathVariable String chatId, @PathVariable String chatRecordId) {
        return R.success(chatRecordService.getChatRecordInfo(chatId, chatRecordId));
    }

    @Hidden
    @PutMapping("/historical_conversation/{chatId}")
    public R<Boolean> updateConversation(@PathVariable String chatId, @RequestBody ApplicationChatEntity chatEntity) {
        chatEntity.setId(chatId);
        return R.success(chatService.updateById(chatEntity));
    }

    @Hidden
    @DeleteMapping("/historical_conversation/{chatId}")
    public R<Boolean> deleteConversation(@PathVariable String chatId) {
        return R.success(chatService.deleteById(chatId));
    }

    @Hidden
    @DeleteMapping("/historical_conversation/clear")
    public R<Boolean> historicalConversationClear() {
        return R.success(chatApiService.historicalConversationClear());
    }

    @Hidden
    @GetMapping("/historical_conversation_record/{chatId}/{current}/{size}")
    public R<IPage<ApplicationChatRecordVO>> historicalConversationRecord(@PathVariable String chatId, @PathVariable int current, @PathVariable int size) {
        return R.success(chatRecordService.chatRecordPage(chatId, current, size));
    }

    @Hidden
    @PutMapping("/vote/chat/{chatId}/chat_record/{chatRecordId}")
    public R<Boolean> updateConversation(@PathVariable String chatId, @PathVariable String chatRecordId, @RequestBody ApplicationChatRecordEntity chatRecord) {
        return R.success(chatApiService.updateConversation(chatId, chatRecordId, chatRecord));
    }

    @Hidden
    @PostMapping("/speech_to_text")
    public R<String> speechToText(MultipartFile file) throws IOException {
        StpKit.USER.setTokenValue(WebUtil.getTokenValue());
        String appId = (String) StpKit.USER.getExtra("applicationId");
        return R.data(applicationService.speechToText(appId, file, false));
    }


    @Hidden
    @PostMapping("/text_to_speech")
    public ResponseEntity<byte[]> textToSpeech(@RequestBody JSONObject data) {
        // 设置 HTTP 响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("audio/mp3"));
        StpKit.USER.setTokenValue(WebUtil.getTokenValue());
        String appId = (String) StpKit.USER.getExtra("applicationId");
        return new ResponseEntity<>(applicationService.textToSpeech(appId, data, false), headers, HttpStatus.OK);
    }


    /**
     * 嵌入第三方
     *
     * @param dto dto
     */
    @Hidden
    @GetMapping("/embed")
    @SaIgnore
    public ResponseEntity<String> embed(EmbedDTO dto) {
        return ResponseEntity.ok().header("Content-Type", "text/javascript; charset=utf-8").body(applicationService.embed(dto));
    }


    @SaCheckPerm(PermissionEnum.APPLICATION_READ)
    @PostMapping("/{id}/chat/{chatId}/share_chat")
    public R<Map<String, String>> shareChat(@PathVariable String id, @PathVariable String chatId, @RequestBody ShareChatDTO dto) {
        return R.success(chatService.shareChat(id, chatId, dto));
    }

    @GetMapping("/share/{id}")
    public R<ShareChatVO> shareChat(@PathVariable String id) {
        return R.success(chatService.shareChat(id));
    }


    // ==================== 批量评测 ====================

    /**
     * 获取所有可用应用列表（公开接口，供批量评测页面下拉选择）
     */
    @Operation(summary = "批量评测 - 获取应用列表", description = "返回所有已发布应用的名称和accessToken，供评测页面选择")
    @SaIgnore
    @GetMapping("/batch_eval/apps")
    public R<List<Map<String, String>>> batchEvalApps() {
        List<ApplicationAccessTokenEntity> tokens = accessTokenService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ApplicationAccessTokenEntity>()
                        .eq(ApplicationAccessTokenEntity::getIsActive, true)
        );
        List<Map<String, String>> result = tokens.stream().map(t -> {
            ApplicationEntity app = applicationService.getById(t.getApplicationId());
            if (app == null) return null;
            return Map.of(
                    "id", app.getId(),
                    "name", app.getName(),
                    "accessToken", t.getAccessToken()
            );
        }).filter(java.util.Objects::nonNull).toList();
        return R.success(result);
    }

    /**
     * 上传测试集，提交批量评测任务
     * 支持格式：CSV / JSONL / TXT
     * - CSV：每行一个问题，或包含 question/问题/q 列头的多列文件
     * - JSONL：每行一个JSON对象，需包含 question / q / input 字段
     * - TXT：每行一个问题
     */
    @Operation(summary = "批量评测 - 上传测试集", description = "上传测试集文件（CSV/JSONL/TXT），返回任务ID，可轮询进度后下载结果")
    @SaIgnore
    @PostMapping("/batch_eval/submit")
    public R<Map<String, Object>> batchEvalSubmit(
            @RequestParam("file") MultipartFile file,
            @RequestParam("accessToken") String accessToken) throws IOException {
        ApplicationAccessTokenEntity tokenEntity = accessTokenService.getByAccessToken(accessToken);
        if (tokenEntity == null) {
            return R.fail("accessToken 不存在");
        }
        String appId = tokenEntity.getApplicationId();
        List<String> questions = batchEvalService.parseQuestions(file.getBytes(), file.getOriginalFilename());
        if (questions.isEmpty()) {
            return R.fail("未解析到有效问题，请检查文件格式");
        }
        String taskId = batchEvalService.submitTask(appId, questions);
        return R.success(Map.of("taskId", taskId, "total", questions.size()));
    }

    /**
     * 查询批量评测任务进度
     */
    @Operation(summary = "批量评测 - 查询进度", description = "轮询任务进度，done=total 时可下载结果")
    @SaIgnore
    @GetMapping("/batch_eval/{taskId}/progress")
    public R<Map<String, Object>> batchEvalProgress(@PathVariable String taskId) {
        BatchEvalService.TaskInfo info = batchEvalService.getTaskInfo(taskId);
        if (info == null) {
            return R.fail("任务不存在: " + taskId);
        }
        return R.success(Map.of(
                "taskId", taskId,
                "total", info.getTotal(),
                "done", info.getDone(),
                "finished", info.isDone()
        ));
    }

    /**
     * 下载批量评测结果 CSV
     */
    @Operation(summary = "批量评测 - 下载结果", description = "任务完成后调用此接口下载评测结果 CSV 文件")
    @SaIgnore
    @GetMapping("/batch_eval/{taskId}/download")
    public ResponseEntity<byte[]> batchEvalDownload(@PathVariable String taskId) {
        byte[] csvBytes = batchEvalService.getResultBytes(taskId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", "eval_result_" + taskId.substring(0, 8) + ".csv");
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

}
