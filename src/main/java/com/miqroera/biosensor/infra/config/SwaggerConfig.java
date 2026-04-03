package com.miqroera.biosensor.infra.config;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class SwaggerConfig {

    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags() != null) {
                openApi.getTags().forEach(tag -> {
                    ApiSupport apiSupport = tag.getClass().getDeclaredAnnotation(ApiSupport.class);
                    if (apiSupport != null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("x-order", apiSupport.order());
                        tag.setExtensions(map);
                    }
                });
            }
        };
    }
}
