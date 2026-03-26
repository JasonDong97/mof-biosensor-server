package com.miqroera.biosensor.infra.domain.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 基础异常
 *
 * @author ruoyi
 */
public class BaseException extends RuntimeException {

    public BaseException(String module, String key, Object... args) {
        super(getMessage(module, key, args));
    }

    public BaseException(String key, Object... args) {
        this(null, key, args);
    }

    /**
     * 获取异常消息
     *
     * @param module 模块
     * @param key    消息键
     * @param args   参数
     * @return 消息
     */
    private static String getMessage(String module, String key, Object... args) {
        String message = StrUtil.format(key, args);

        if (StrUtil.isNotBlank(module)) {
            message = StrUtil.format("[{}]{}", module, message);
        }

        return message;
    }

}
