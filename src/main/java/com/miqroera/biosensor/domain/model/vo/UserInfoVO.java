package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息响应 VO
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Data
@Builder
@Schema(name = "UserInfoVO", description = "用户信息响应")
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID", example = "1")
    private Long id;

    /**
     * 微信 OPENID
     */
    @Schema(description = "微信 OPENID")
    private String openid;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号", example = "13800138000")
    private String userName;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "张三")
    private String nickName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 用户性别（0-未知 1-男 2-女）
     */
    @Schema(description = "用户性别（0-未知 1-男 2-女）", example = "1")
    private String sex;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "138****1234")
    private String phonenumber;

    /**
     * 用户类型（0-普通用户 1-管理员）
     */
    @Schema(description = "用户类型（0-普通用户 1-管理员）", example = "0")
    private String userType;

    /**
     * 出生日期
     */
    @Schema(description = "出生日期")
    private LocalDate birthday;

    /**
     * 身高(cm)
     */
    @Schema(description = "身高(cm)")
    private Float height;

    /**
     * 体重(kg)
     */
    @Schema(description = "体重(kg)")
    private Float weight;

    /**
     * 首次检测日期
     */
    @Schema(description = "首次检测日期")
    private LocalDate firstMeasureDate;

    /**
     * 累计检测次数
     */
    @Schema(description = "累计检测次数")
    private Integer totalMeasures;
}
