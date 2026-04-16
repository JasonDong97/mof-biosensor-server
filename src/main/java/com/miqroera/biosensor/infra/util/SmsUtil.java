package com.miqroera.biosensor.infra.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyun.auth.credentials.provider.DefaultCredentialProvider;
import com.aliyun.auth.credentials.provider.SystemPropertiesCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.miqroera.biosensor.infra.config.SmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsUtil {

    /**
     * 发送短信验证码
     *
     * @param smsConfig 短信配置
     * @return 发送结果
     */
    @SneakyThrows
    public static SendSmsResponse sendCode(String phoneNumber,String code, SmsConfig smsConfig) {
        // Configure Credentials authentication information
        if (smsConfig == null) {
            log.error("SmsConfig is null");
            return null;
        }

        // Configure the Client
        try (AsyncClient client = getClient(smsConfig)) {
            // Parameter settings for API request
            JSONObject params = JSON.parseObject(smsConfig.getTemplateParam());
            params.put("code", code);
            SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                    .phoneNumbers(phoneNumber)
                    .signName(smsConfig.getSignName())
                    .templateCode(smsConfig.getTemplateCode())
                    .templateParam(params.toJSONString())
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();

            return client.sendSms(sendSmsRequest).get();
        }
    }

    private static AsyncClient getClient(SmsConfig config) {
        System.setProperty("alibabacloud.accessKeyId", config.getAccessKeyId());
        System.setProperty("alibabacloud.accessKeyIdSecret", config.getAccessKeySecret());
        DefaultCredentialProvider provider = DefaultCredentialProvider.builder()
                .customizeProviders(SystemPropertiesCredentialProvider.create())
                .build();
        return AsyncClient.builder()
                .region("cn-shanghai") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();
    }
}
