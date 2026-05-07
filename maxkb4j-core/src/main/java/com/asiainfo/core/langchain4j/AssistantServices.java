package com.asiainfo.core.langchain4j;

import com.asiainfo.core.listener.AssistantCompletedListener;
import com.asiainfo.core.listener.AssistantErrorListener;
import com.asiainfo.core.listener.AssistantStartedListener;
import com.asiainfo.core.listener.AssistantToolExecutedEventListener;
import dev.langchain4j.observability.api.listener.AiServiceListener;
import dev.langchain4j.service.AiServices;

import java.util.Arrays;
import java.util.Collection;

public class AssistantServices {

    private static final Collection<AiServiceListener<?>> LISTENERS = Arrays.asList(
            new AssistantStartedListener(),
            new AssistantCompletedListener(),
            new AssistantToolExecutedEventListener(),
            new AssistantErrorListener()
    );

    public static <T> AiServices<T> builder(Class<T> aiService) {
        return AiServices.builder(aiService).registerListeners(LISTENERS);
    }

}
