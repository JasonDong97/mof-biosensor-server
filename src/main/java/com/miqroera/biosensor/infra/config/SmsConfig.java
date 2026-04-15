package com.miqroera.biosensor.infra.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sms.aliyun")
public class SmsConfig {

    /**
     * 阿里云访问密钥ID
     */
    private String accessKeyId;

    /**
     * 阿里云访问密钥Secret
     */
    private String accessKeySecret;

    /**
     * 短信签名名称
     */
    private String signName;

    /**
     * 短信模板CODE
     */
    private String templateCode;

    /**
     * 短信模板参数
     */
    private String templateParam;

    @PostConstruct
    public void init(){
        System.setProperty("alibabacloud.accessKeyId", accessKeyId);
        System.setProperty("alibabacloud.accessKeyIdSecret", accessKeySecret);
    }
}
