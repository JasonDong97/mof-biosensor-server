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

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 检测记录表
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Getter
@Setter
@ToString
@TableName("t_record")
@Schema(name = "Record", description = "检测记录表")
public class Record implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @Schema(description = "主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户端生成的唯一记录 ID(UUID)
     */
    @Schema(description = "客户端生成的唯一记录 ID(UUID)")
    private String recordId;

    /**
     * 用户 ID
     */
    @Schema(description = "用户 ID")
    private Long userId;

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
     * 等级文案（如\"低风险\"）
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
     * 固件版本
     */
    @Schema(description = "固件版本")
    private String firmwareVersion;

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
     * 记录创建时间
     */
    @Schema(description = "记录创建时间")
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
