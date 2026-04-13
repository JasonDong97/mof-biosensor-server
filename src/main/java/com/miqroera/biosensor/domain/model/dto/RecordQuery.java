package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 检测记录查询参数 DTO
 *
 * @author dongjingxiang
 * @since 2026-04-01
 */
@Data
@Schema(name = "RecordQuery", description = "检测记录查询参数")
public class RecordQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页码（默认 1）
     */
    @Schema(description = "页码", example = "1", defaultValue = "1")
    private Integer pageNum = 1;

    /**
     * 每页数量（默认 10）
     */
    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private Integer pageSize = 10;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2026-01-01")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2026-12-31")
    private LocalDate endTime;

    /**
     * 场景标签（0 未选 1 空腹 2 餐后 3 运动后）
     */
    @Schema(description = "场景标签（0 未选 1 空腹 2 餐后 3 运动后）", example = "1")
    private Byte sceneType;

    /**
     * 设备 SN
     */
    @Schema(description = "设备 SN", example = "DEV20260331001")
    private String deviceSn;

    /**
     * 等级（1-5）
     */
    @Schema(description = "等级（1-5）", example = "2")
    private Byte level;
}