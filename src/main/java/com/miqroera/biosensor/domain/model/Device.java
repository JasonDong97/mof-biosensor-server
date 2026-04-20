package com.miqroera.biosensor.domain.model;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 设备信息表
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Getter
@Setter
@ToString
@TableName("t_device")
@Schema(name = "Device", description = "设备信息表")
public class Device implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID
     */
    @Schema(description = "主键 ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备 SN 码
     */
    @Schema(description = "设备 SN 码")
    private String deviceSn;

    /**
     * MAC 地址
     */
    @Schema(description = "MAC 地址")
    private String mac;

    /**
     * 固件版本
     */
    @Schema(description = "固件版本")
    private String firmwareVersion;

    /**
     * 硬件版本
     */
    @Schema(description = "硬件版本")
    private String hardwareVersion;

    /**
     * 生产批次
     */
    @Schema(description = "生产批次")
    private String batchNo;

    /**
     * 激活时间（首次绑定）
     */
    @Schema(description = "激活时间（首次绑定）")
    private LocalDateTime activatedAt;

    /**
     * 最后使用时间
     */
    @Schema(description = "最后使用时间")
    private LocalDateTime lastUsedAt;

    /**
     * 累计使用次数
     */
    @Schema(description = "累计使用次数")
    private Integer totalUses;

    /**
     * 设备状态（0 正常 1 禁用）
     */
    @Schema(description = "设备状态（0 正常 1 禁用）")
    private String status;

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

    @Schema(description = "蓝牙名称")
    private String bluetoothName;

    @Schema(description = "蓝牙MAC")
    private String bluetoothMac;
}
