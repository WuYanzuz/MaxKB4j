package com.asiainfo.knowledge.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 文档目录监控配置
 * 配置前缀: maxkb4j.document-watch
 */
@Data
@Component
@ConfigurationProperties(prefix = "maxkb4j.document-watch")
public class DocumentWatchProperties {

    /**
     * 是否启用目录监控
     */
    private boolean enabled = false;

    /**
     * 扫描间隔（秒），默认 60 秒
     */
    private int intervalSeconds = 60;

    /**
     * 监控的目录列表
     */
    private List<WatchDirectory> directories = new ArrayList<>();

    @Data
    public static class WatchDirectory {

        /**
         * 要监控的本地目录路径
         */
        private String path;

        /**
         * 同步到的知识库ID
         */
        private String knowledgeId;

        /**
         * 备注名称（可选）
         */
        private String name;
    }
}
