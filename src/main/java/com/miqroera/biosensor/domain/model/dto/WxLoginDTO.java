package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小程序微信登录请求 DTO
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Data
@Schema(name = "WxLoginDTO", description = "微信小程序登录请求参数")
public class WxLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 微信小程序登录 code（通过 wx.login() 获取）
     */
    @NotBlank(message = "微信 code 不能为空")
    @Schema(description = "微信小程序登录 code", example = "071xxx123", required = true)
    private String code;

    /**
     * 用户昵称（可选，首次注册时使用）
     */
    @Schema(description = "用户昵称", example = "张三")
    private String nickname;

    /**
     * 用户头像（可选，首次注册时使用）
     */
    @Schema(description = "用户头像 URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 用户性别（可选，首次注册时使用，0-未知 1-男 2-女）
     */
    @Schema(description = "用户性别（0-未知 1-男 2-女）", example = "1")
    @Range(min = 0, max = 2)
    private Integer gender;


    @Schema(description = "用户手机号", example = "13800000000")
    private String phonenumber ;
}
