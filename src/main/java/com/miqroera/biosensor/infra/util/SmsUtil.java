package com.miqroera.biosensor.infra.util;

import com.aliyun.auth.credentials.provider.DefaultCredentialProvider;
import com.aliyun.auth.credentials.provider.SystemPropertiesCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.google.gson.Gson;
import com.miqroera.biosensor.infra.config.SmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class SmsUtil {

    /**
     * 发送短信验证码
     *
     * @param smsConfig
     * @return 发送结果
     */
    @SneakyThrows
    public static String sendCode(String phoneNumber,  SmsConfig smsConfig) {
        // Configure Credentials authentication information
        if (smsConfig == null) {
            log.error("SmsConfig is null");
            return null;
        }

        System.setProperty("alibabacloud.accessKeyId", smsConfig.getAccessKeyId());
        System.setProperty("alibabacloud.accessKeyIdSecret", smsConfig.getAccessKeySecret());
        DefaultCredentialProvider provider = DefaultCredentialProvider.builder()
                .customizeProviders(SystemPropertiesCredentialProvider.create())
                .build();

        // Configure the Client
        try (AsyncClient client = AsyncClient.builder()
                .region("cn-shanghai") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
                                .setEndpointOverride("dypnsapi.aliyuncs.com")
                        //.setConnectTimeout(Duration.ofSeconds(30))
                )
                .build()) {

            // Parameter settings for API request
            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .phoneNumber(phoneNumber)
                    .signName(smsConfig.getSignName())
                    .templateCode(smsConfig.getTemplateCode())
                    .templateParam(smsConfig.getTemplateParam())
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();

            // Asynchronously get the return value of the API request
            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            // Synchronously get the return value of the API request
            SendSmsVerifyCodeResponse resp = response.get();
            return new Gson().toJson(resp);
        }
    }
}
