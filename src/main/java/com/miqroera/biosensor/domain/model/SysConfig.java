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
 * 系统配置表
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Getter
@Setter
@ToString
@TableName("sys_config")
@Schema(name = "SysConfig", description = "系统配置表")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @Schema(description = "主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 配置键
     */
    @Schema(description = "配置键")
    private String configKey;

    /**
     * 配置值（可为 JSON）
     */
    @Schema(description = "配置值（可为 JSON）")
    private String configValue;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 配置类型（0 普通 1 敏感）
     */
    @Schema(description = "配置类型（0 普通 1 敏感）")
    private String configType;

    /**
     * 删除标志（0 代表存在 1 代表删除）
     */
    @Schema(description = "删除标志（0 代表存在 1 代表删除）")
    private String delFlag;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @Schema(description = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
