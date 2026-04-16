package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检测记录详情响应 VO（关联设备信息）
 *
 * @author dongjingxiang
 * @since 2026-04-16
 */
@Data
@Builder
@Schema(name = "RecordDetailVO", description = "检测记录详情响应")
public class RecordDetailVO implements Serializable {

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
     * 设备固件版本
     */
    @Schema(description = "设备固件版本")
    private String firmwareVersion;

    /**
     * 设备硬件版本
     */
    @Schema(description = "设备硬件版本")
    private String hardwareVersion;

    /**
     * 设备状态（0 正常 1 禁用）
     */
    @Schema(description = "设备状态（0 正常 1 禁用）")
    private String deviceStatus;

    /**
     * 检测时间
     */
    @Schema(description = "检测时间")
    private LocalDateTime timestamp;

    /**
     * 场景标签（0 未选 1 空腹 2 餐后 3 运动后）
     */
    @Schema(description = "场景标签（0 未选 1 空腹 2 餐后 3 运动后）")
    private Integer sceneType;

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
     * 基线电阻
     */
    @Schema(description = "基线电阻")
    private Double rBase;

    /**
     * 响应电阻
     */
    @Schema(description = "响应电阻")
    private Double rGas;

    /**
     * ADC 采样值
     */
    @Schema(description = "ADC 采样值")
    private Integer adcValue;

    /**
     * 环境温度 (℃)
     */
    @Schema(description = "环境温度 (℃)")
    private Double temperature;

    /**
     * 环境湿度 (%RH)
     */
    @Schema(description = "环境湿度 (%RH)")
    private Double humidity;

    /**
     * 加热片温度 (℃)
     */
    @Schema(description = "加热片温度 (℃)")
    private Double heaterTemp;

    /**
     * 算法版本
     */
    @Schema(description = "算法版本")
    private String algoVersion;

    /**
     * 气体类型（如 H2）
     */
    @Schema(description = "气体类型（如 H2）")
    private String gasType;

    /**
     * 扩展字段
     */
    @Schema(description = "扩展字段")
    private String extraData;

    /**
     * 记录创建时间
     */
    @Schema(description = "记录创建时间")
    private LocalDateTime createTime;
}
