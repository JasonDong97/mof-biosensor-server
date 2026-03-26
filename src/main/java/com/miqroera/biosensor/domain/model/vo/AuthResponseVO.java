package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 认证响应 VO（Token + 用户信息）
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Data
@Builder
@Schema(name = "AuthResponseVO", description = "认证响应信息")
public class AuthResponseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Token 信息
     */
    @Schema(description = "Token 信息")
    private TokenVO token;

    /**
     * 用户信息
     */
    @Schema(description = "用户信息")
    private UserInfoVO userInfo;
}
