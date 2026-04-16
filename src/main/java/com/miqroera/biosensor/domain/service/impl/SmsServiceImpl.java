package com.miqroera.biosensor.domain.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.miqroera.biosensor.domain.service.ISmsService;
import com.miqroera.biosensor.infra.config.SmsConfig;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.util.RedisUtil;
import com.miqroera.biosensor.infra.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 短信服务实现类
 *
 * @author dongjingxiang
 * @since 2026-04-14
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SmsServiceImpl implements ISmsService {

    /**
     * 验证码 Redis Key 前缀
     */
    private static final String SMS_CODE_PREFIX = "sms:code:";
    /**
     * 验证码有效期：5分钟
     */
    private static final long SMS_CODE_EXPIRE_SECONDS = 300;
    /**
     * 验证码发送间隔：60秒
     */
    private static final long SMS_SEND_INTERVAL_SECONDS = 60;
    /**
     * 验证码发送间隔 Redis Key 前缀
     */
    private static final String SMS_SEND_INTERVAL_PREFIX = "sms:send:interval:";
    private final RedisUtil redisUtil;
    private final SmsConfig smsConfig;

    @Override
    public SendSmsResponse sendCode(String phoneNumber) {
        // 参数校验
        validatePhoneNumber(phoneNumber);

        // 检查发送间隔
        // checkSendInterval(phoneNumber);

        // 生成验证码
        String code = generateCode();
        log.info("生成验证码: {} for phone: {}", code, phoneNumber);

        // 验证码存入 Redis
        String codeKey = SMS_CODE_PREFIX + phoneNumber;
        redisUtil.set(codeKey, code, SMS_CODE_EXPIRE_SECONDS);

        // 设置发送间隔
        String intervalKey = SMS_SEND_INTERVAL_PREFIX + phoneNumber;
        redisUtil.set(intervalKey, "1", SMS_SEND_INTERVAL_SECONDS);

        // 调用阿里云发送短信
        SendSmsResponse sendSmsResponse = SmsUtil.sendCode(phoneNumber, code, smsConfig);
        log.info("发送短信到 {}， 响应结果: {}", phoneNumber, JSON.toJSONString(sendSmsResponse, JSONWriter.Feature.PrettyFormat));

        return sendSmsResponse;
    }

    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        if (StringUtils.isBlank(phoneNumber) || StringUtils.isBlank(code)) {
            return false;
        }
        // 从 Redis 获取存储的验证码
        String codeKey = SMS_CODE_PREFIX + phoneNumber;
        Object storedCodeObj = redisUtil.get(codeKey);
        if (storedCodeObj == null) {
            log.warn("验证码已过期或不存在: phone={}", phoneNumber);
            return false;
        }
        // 验证码比对
        String storedCode = storedCodeObj.toString();
        boolean matched = storedCode.equals(code);
        if (matched) {
            log.info("验证码校验成功: phone={}", phoneNumber);
            // 验证成功后删除验证码，防止重复使用
            redisUtil.del(codeKey);
        } else {
            log.warn("验证码错误: phone={}", phoneNumber);
        }
        return matched;
    }

    /**
     * 校验手机号
     */
    private void validatePhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            throw ServiceException.of("手机号不能为空");
        }
        if (!phoneNumber.matches("^1[3-9]\\d{9}$")) {
            throw ServiceException.of("手机号格式不正确");
        }
    }

    /**
     * 检查发送间隔
     */
    private void checkSendInterval(String phoneNumber) {
        String intervalKey = SMS_SEND_INTERVAL_PREFIX + phoneNumber;
        if (redisUtil.hasKey(intervalKey)) {
            long expire = redisUtil.getExpire(intervalKey);
            throw ServiceException.of("发送过于频繁，请在 {} 秒后重试", expire);
        }
    }

    /**
     * 生成6位数字验证码
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
