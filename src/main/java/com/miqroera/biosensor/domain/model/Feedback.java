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
 * 用户反馈表
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Getter
@Setter
@ToString
@TableName("t_feedback")
@Schema(name = "Feedback", description = "用户反馈表")
public class Feedback implements Serializable {

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
     * 反馈内容
     */
    @Schema(description = "反馈内容")
    private String content;

    /**
     * 图片 URL 列表
     */
    @Schema(description = "图片 URL 列表")
    private String images;

    /**
     * 管理员回复
     */
    @Schema(description = "管理员回复")
    private String reply;

    /**
     * 回复时间
     */
    @Schema(description = "回复时间")
    private LocalDateTime replyTime;

    /**
     * 回复人
     */
    @Schema(description = "回复人")
    private String replyBy;

    /**
     * 状态（0 待回复 1 已回复）
     */
    @Schema(description = "状态（0 待回复 1 已回复）")
    private Byte status;

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
     * 提交时间
     */
    @Schema(description = "提交时间")
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
