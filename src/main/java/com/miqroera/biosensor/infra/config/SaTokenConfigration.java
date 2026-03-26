package com.miqroera.biosensor.infra.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)  // 最高优先级
public class SaTokenConfigration implements WebMvcConfigurer {
    /**
     * 排除路径
     */
    private static final String[] EXCLUDE_PATH_PATTERNS = {
            "/favicon.ico",
            "/api/v1/auth/login/**",  // 登录接口
            "/api/v1/auth/logout",    // 登出接口
            "/api/v1/auth/refresh",   // 刷新 Token 接口
            "/doc.html*/**",          // Knife4j 文档页面
            "/swagger-ui/**",         // Swagger UI 资源
            "/swagger-resources/**",  // Swagger 资源
            "/v2/api-docs",           // OpenAPI v2 JSON
            "/v3/api-docs/**",        // OpenAPI v3 JSON
            "/webjars/**",            // WebJars 静态资源
            "/actuator/**",           // Actuator 端点
            "/oauth2/**",             // OAuth2 相关
            "/error/**"               // 错误页面
    };

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /*
     * 配置 Sa-Token
     */
    @Bean
    @Primary
    public SaTokenConfig getSaTokenConfigPrimary() {
        SaTokenConfig config = new SaTokenConfig();
        config.setTokenName("Authorization");
        config.setTokenPrefix("Bearer ");
        config.setTimeout(7 * 24 * 60 * 60);
        config.setActiveTimeout(-1);
        config.setIsConcurrent(true);
        config.setIsShare(false);
        config.setTokenStyle("tik");
        config.setIsLog(false);
        config.setCookieAutoFillPrefix(true);
        return config;
    }

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 使用数组配置排除路径
        registry.addInterceptor(new SaInterceptor(handler -> {
                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);

        log.info("SaToken 拦截器配置完成，排除路径：{}", String.join(", ", EXCLUDE_PATH_PATTERNS));
    }

    // 添加资源处理器，暴露 Knife4j 和 Swagger 的静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Knife4j 和 Swagger UI 静态资源 (带 context-path: /api)
        registry.addResourceHandler(contextPath + "/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600);

        registry.addResourceHandler(contextPath + "/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-ui/")
                .setCachePeriod(3600);

        registry.addResourceHandler(contextPath + "/swagger-resources/**")
                .addResourceLocations("classpath:/META-INF/resources/swagger-resources/")
                .setCachePeriod(3600);

        // Knife4j 主页面
        registry.addResourceHandler(contextPath + "/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/doc.html")
                .setCachePeriod(3600);

        // OpenAPI JSON
        registry.addResourceHandler(contextPath + "/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v3/api-docs/")
                .setCachePeriod(3600);
    }
}
