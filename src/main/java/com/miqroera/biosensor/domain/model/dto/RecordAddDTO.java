package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检测记录上报 DTO
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Data
@Schema(name = "RecordAddDTO", description = "检测记录上报参数")
public class RecordAddDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户端生成的唯一记录 ID(UUID)
     */
    @NotBlank(message = "记录 ID 不能为空")
    @Schema(description = "客户端生成的唯一记录 ID(UUID)", example = "550e8400-e29b-41d4-a716-446655440000", required = true)
    private String recordId;

    /**
     * 设备 SN
     */
    @NotBlank(message = "设备 SN 不能为空")
    @Schema(description = "设备 SN", example = "DEV20260331001", required = true)
    private String deviceSn;

    /**
     * 检测时间
     */
    @NotNull(message = "检测时间不能为空")
    @Schema(description = "检测时间", example = "2026-03-31 10:30:00", required = true)
    private LocalDateTime timestamp;

    /**
     * 场景标签（0 未选 1 空腹 2 餐后 3 运动后）
     */
    @Schema(description = "场景标签（0 未选 1 空腹 2 餐后 3 运动后）", example = "1", defaultValue = "0")
    private Integer sceneType = 0;

    /**
     * 浓度值 (ppb)
     */
    @Schema(description = "浓度值 (ppb)", example = "15.5")
    private Double concentration;

    /**
     * 等级（1-5）
     */
    @Schema(description = "等级（1-5）", example = "2")
    private Byte level;

    /**
     * 等级文案（如"低风险"）
     */
    @Schema(description = "等级文案（如\"低风险\"）", example = "低风险")
    private String levelLabel;

    /**
     * 建议文案
     */
    @Schema(description = "建议文案", example = "检测结果正常，请继续保持")
    private String suggestion;

    /**
     * 基线电阻
     */
    @Schema(description = "基线电阻", example = "10000.5")
    private Double rBase;

    /**
     * 响应电阻
     */
    @Schema(description = "响应电阻", example = "8500.2")
    private Double rGas;

    /**
     * ADC 采样值
     */
    @Schema(description = "ADC 采样值", example = "2048")
    private Integer adcValue;

    /**
     * 环境温度 (℃)
     */
    @Schema(description = "环境温度 (℃)", example = "25.5")
    private Double temperature;

    /**
     * 环境湿度 (%RH)
     */
    @Schema(description = "环境湿度 (%RH)", example = "60.0")
    private Double humidity;

    /**
     * 加热片温度 (℃)
     */
    @Schema(description = "加热片温度 (℃)", example = "150.0")
    private Double heaterTemp;

    /**
     * 算法版本
     */
    @Schema(description = "算法版本", example = "v1.0.0")
    private String algoVersion;

    /**
     * 固件版本
     */
    @Schema(description = "固件版本", example = "v2.1.0")
    private String firmwareVersion;

    /**
     * 气体类型（如 H2）
     */
    @Schema(description = "气体类型（如 H2）", example = "H2")
    private String gasType;

    /**
     * 扩展字段（JSON 格式）
     */
    @Schema(description = "扩展字段（JSON 格式）", example = "{\"key\":\"value\"}")
    private String extraData;
}