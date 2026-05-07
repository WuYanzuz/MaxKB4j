package com.asiainfo.application.builder;


import com.asiainfo.application.enums.AppType;
import com.asiainfo.application.service.IChatService;
import com.asiainfo.application.service.impl.ChatFlowServiceImpl;
import com.asiainfo.application.service.impl.ChatSimpleServiceImpl;
import com.asiainfo.common.exception.ApiException;
import com.asiainfo.common.util.SpringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServiceBuilder {

    private static final Map<String, IChatService> ACTUATOR_POOL = new ConcurrentHashMap<>();

    static {
        ACTUATOR_POOL.put(AppType.SIMPLE.name(), SpringUtil.getBean(ChatSimpleServiceImpl.class));
        ACTUATOR_POOL.put(AppType.WORK_FLOW.name(), SpringUtil.getBean(ChatFlowServiceImpl.class));
    }

    /**
     * 获取ChatActuator
     *
     * @param appType 应用类型
     * @return IChatActuator
     */
    public static IChatService getChatService(String appType) {
        IChatService chatActuator = ACTUATOR_POOL.get(appType);
        if (chatActuator == null) {
            throw new ApiException("no appType was found");
        } else {
            return ACTUATOR_POOL.get(appType);
        }
    }
}
