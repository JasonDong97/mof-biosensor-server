package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检测记录列表响应 VO
 *
 * @author dongjingxiang
 * @since 2026-04-01
 */
@Data
@Builder
@Schema(name = "RecordListVO", description = "检测记录列表响应")
public class RecordListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @Schema(description = "主键 ID")
    private Long id;

    /**
     * 客户端生成的唯一记录 ID(UUID)
     */
    @Schema(description = "客户端生成的唯一记录 ID(UUID)")
    private String recordId;

    /**
     * 设备 SN
     */
    @Schema(description = "设备 SN")
    private String deviceSn;

    /**
     * 检测时间
     */
    @Schema(description = "检测时间")
    private LocalDateTime timestamp;

    /**
     * 场景标签（0 未选 1 空腹 2 餐后 3 运动后）
     */
    @Schema(description = "场景标签（0 未选 1 空腹 2 餐后 3 运动后）")
    private Byte sceneType;

    /**
     * 浓度值 (ppb)
     */
    @Schema(description = "浓度值 (ppb)")
    private Double concentration;

    /**
     * 等级（1-5）
     */
    @Schema(description = "等级（1-5）")
    private Byte level;

    /**
     * 等级文案（如"低风险"）
     */
    @Schema(description = "等级文案（如\"低风险\"）")
    private String levelLabel;

    /**
     * 建议文案
     */
    @Schema(description = "建议文案")
    private String suggestion;

    /**
     * 记录创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}