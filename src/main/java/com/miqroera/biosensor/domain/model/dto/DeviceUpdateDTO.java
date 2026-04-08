package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 设备修改请求 DTO
 *
 * @author dongjingxiang
 * @since 2026-04-08
 */
@Data
@Schema(name = "DeviceUpdateDTO", description = "设备修改请求参数")
public class DeviceUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 设备状态（0 正常 1 禁用）
     */
    @Schema(description = "设备状态（0 正常 1 禁用）", example = "0")
    private String status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}