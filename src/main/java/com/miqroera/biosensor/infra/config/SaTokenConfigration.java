package com.miqroera.biosensor.infra.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SaTokenConfigration implements WebMvcConfigurer {

    /**
     * 排除路径（同时配置带和不带 context-path 的版本）
     */
    private static final String[] EXCLUDE_PATH_PATTERNS = {
            "/favicon.ico",
            "/doc.html*/**",          // Knife4j 文档页面
            "/webjars/**",            // WebJars 静态资源
            "/swagger-ui.html*/**",          // Knife4j 文档页面
            "/swagger-ui/**",         // Swagger UI 资源
            "/swagger-resources/**",  // Swagger 资源
            "/v3/api-docs/**",        // OpenAPI v3 JSON
            "/actuator/**",           // Actuator 端点
            "/v1/auth/**"             // 认证接口
    };

    /*
     * 配置 Sa-Token 全局过滤器
     */
    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        config.setTokenName("Authorization");
        config.setTokenPrefix("Bearer");
        config.setTimeout(7 * 24 * 60 * 60);
        config.setActiveTimeout(-1);
        config.setIsConcurrent(true);
        config.setIsShare(false);
        config.setTokenStyle("tik");
        config.setIsLog(false);
        config.setCookieAutoFillPrefix(true);
        return config;
    }

    /**
     * 注册 Sa-Token 全局过滤器
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude(EXCLUDE_PATH_PATTERNS)
                .setAuth(obj -> {
                    // 登录校验
                    StpUtil.checkLogin();
                })
                .setError(e -> {
                    // 异常处理
                    return JSON.toJSONString(GlobalExceptionHandler.toR(401, e, "未登录", true));
                });
    }

    // 添加资源处理器，暴露 Knife4j 和 Swagger 的静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 启用默认的资源处理器（重要！处理所有静态资源）
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/",
                        "classpath:/resources/", "classpath:/META-INF/resources/")
                .setCachePeriod(3600);
    }
}
