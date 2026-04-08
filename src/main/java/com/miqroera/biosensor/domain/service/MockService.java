package com.miqroera.biosensor.domain.service;

import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.domain.model.MockProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MockService {

    private final MockProperties mockProperties;

    /**
     * 模拟获取 openid
     *
     * @param code 微信小程序登录 code
     * @return openid
     */
    public String mockGetOpenId(String code) {

        var openid = mockProperties.getOpenids().get(code);
        if (openid == null) {
            throw new ServiceException("无效的 code: {}", code);
        }
        return openid;
    }
}
