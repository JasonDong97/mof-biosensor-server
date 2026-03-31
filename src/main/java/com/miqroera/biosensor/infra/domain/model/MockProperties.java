package com.miqroera.biosensor.infra.domain.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "mock")
public class MockProperties {

    /**
     * 用户信息
     */
    private Map<String, String> openids;
}
