package com.miqroera.biosensor.domain.service;

import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;

/**
 * 短信服务接口
 *
 * @author dongjingxiang
 * @since 2026-04-14
 */
public interface ISmsService {

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号
     * @return 响应结果
     */
    SendSmsResponse sendCode(String phoneNumber);

    /**
     * 验证短信验证码
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     * @return 是否验证通过
     */
    boolean verifyCode(String phoneNumber, String code);
}
