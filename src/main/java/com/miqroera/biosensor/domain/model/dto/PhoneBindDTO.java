package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 手机号绑定请求 DTO
 *
 * @author dongjingxiang
 * @since 2026-04-16
 */
@Data
@Schema(name = "PhoneBindDTO", description = "手机号绑定请求参数")
public class PhoneBindDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号码", example = "13800138000", required = true)
    private String phonenumber;

    /**
     * 短信验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Schema(description = "短信验证码", example = "123456", required = true)
    private String code;
}
