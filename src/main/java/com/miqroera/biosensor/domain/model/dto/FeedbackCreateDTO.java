package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户反馈提交 DTO
 *
 * @author dongjingxiang
 * @since 2026-04-03
 */
@Data
@Schema(name = "FeedbackCreateDTO", description = "用户反馈提交参数")
public class FeedbackCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 反馈内容（必填）
     */
    @NotBlank(message = "反馈内容不能为空")
    @Schema(description = "反馈内容", example = "使用体验很好，希望能增加更多功能", required = true)
    private String content;

    /**
     * 图片 URL 列表（可选，最多 3 张）
     */
    @Schema(description = "图片 URL 列表", example = "[\"https://example.com/img1.jpg\",\"https://example.com/img2.jpg\"]")
    private List<String> images;
}