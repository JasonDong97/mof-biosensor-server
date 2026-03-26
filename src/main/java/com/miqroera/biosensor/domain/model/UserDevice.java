package com.miqroera.biosensor.domain.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户设备绑定关系表
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Getter
@Setter
@ToString
@TableName("t_user_device")
@Schema(name = "UserDevice", description = "用户设备绑定关系表")
public class UserDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @Schema(description = "主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID")
    private Long userId;

    /**
     * 设备 ID
     */
    @Schema(description = "设备 ID")
    private Long deviceId;

    /**
     * 绑定时间
     */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    /**
     * 解绑时间
     */
    @Schema(description = "解绑时间")
    private LocalDateTime unbindTime;

    /**
     * 是否当前绑定（1 是 0 否）
     */
    @Schema(description = "是否当前绑定（1 是 0 否）")
    private Byte isActive;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
