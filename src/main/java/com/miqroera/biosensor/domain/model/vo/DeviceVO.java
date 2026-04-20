package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备信息响应 VO
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Data
@Builder
@Schema(name = "DeviceVO", description = "设备信息响应")
public class DeviceVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备 ID
     */
    @Schema(description = "设备 ID")
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
     * 绑定时间
     */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;

    @Schema(description = "绑定的用户 ID")
    private Long bindUserId;

    @Schema(description = "绑定的用户昵称")
    private String bindUserNickName;

    @Schema(description = "蓝牙名称")
    private String bluetoothName;

    @Schema(description = "蓝牙 MAC 地址")
    private String bluetoothMac;
}