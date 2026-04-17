package com.miqroera.biosensor.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WechatConfig {

    /**
     * 微信公众号appId
     */
    private String appId;

    /**
     * 微信公众号appSecret
     */
    private String appSecret;
}
