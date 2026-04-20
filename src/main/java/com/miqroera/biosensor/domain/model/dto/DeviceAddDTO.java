package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 设备添加请求 DTO
 *
 * @author dongjingxiang
 * @since 2026-04-08
 */
@Data
@Schema(name = "DeviceAddDTO", description = "设备添加请求参数")
public class DeviceAddDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备 SN 码
     */
    @NotBlank(message = "设备 SN 码不能为空")
    @Schema(description = "设备 SN 码", example = "DEV20260331001", required = true)
    private String deviceSn;

    /**
     * MAC 地址
     */
    @Schema(description = "MAC 地址", example = "00:1A:2B:3C:4D:5E")
    private String mac;

    /**
     * 固件版本
     */
    @Schema(description = "固件版本", example = "1.0.0")
    private String firmwareVersion;

    /**
     * 硬件版本
     */
    @Schema(description = "硬件版本", example = "2.0")
    private String hardwareVersion;

    /**
     * 生产批次
     */
    @Schema(description = "生产批次", example = "BATCH20260331")
    private String batchNo;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "蓝牙名称")
    private String bluetoothName;

    @Schema(description = "蓝牙 MAC 地址")
    private String bluetoothMac;
}