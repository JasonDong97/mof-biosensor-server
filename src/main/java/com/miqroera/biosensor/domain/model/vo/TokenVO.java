package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Token 响应 VO
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Data
@Builder
@Schema(name = "TokenVO", description = "Token 响应信息")
public class TokenVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Schema(description = "刷新令牌", example = "def50200a1b2c3d4e5f6...")
    private String refreshToken;

    /**
     * 过期时间（秒）
     */
    @Schema(description = "过期时间（秒）", example = "604800")
    private Long expiresIn;

    /**
     * 令牌类型
     */
    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;
}
