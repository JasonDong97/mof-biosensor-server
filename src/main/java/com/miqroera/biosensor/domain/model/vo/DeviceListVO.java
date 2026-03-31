package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备列表响应 VO
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Data
@Builder
@Schema(name = "DeviceListVO", description = "设备列表响应")
public class DeviceListVO implements Serializable {

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
     * 绑定时间
     */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;
}