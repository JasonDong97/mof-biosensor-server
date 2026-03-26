package com.miqroera.biosensor.infra.domain.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 */
@Getter
public class ServiceException extends BaseException {
    @Setter
    private String originalMessage;

    public ServiceException(String key, Object... args) {
        super(key, args);
    }

    public ServiceException(Exception e, String key, Object... args) {
        super(key, args);
        this.originalMessage = e.getMessage();
    }

    public static ServiceException of(String key, Object... args) {
        return new ServiceException(key, args);
    }
}