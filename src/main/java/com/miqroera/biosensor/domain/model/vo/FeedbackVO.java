package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户反馈响应 VO
 *
 * @author dongjingxiang
 * @since 2026-04-03
 */
@Data
@Builder
@Schema(name = "FeedbackVO", description = "用户反馈响应")
public class FeedbackVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @Schema(description = "主键 ID")
    private Long id;

    /**
     * 反馈内容
     */
    @Schema(description = "反馈内容")
    private String content;

    /**
     * 图片 URL 列表
     */
    @Schema(description = "图片 URL 列表")
    private List<String> images;

    /**
     * 状态（0 待回复 1 已回复）
     */
    @Schema(description = "状态（0 待回复 1 已回复）")
    private Byte status;

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
     * 提交时间
     */
    @Schema(description = "提交时间")
    private LocalDateTime createTime;
}