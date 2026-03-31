package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 设备解绑请求 DTO
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Data
@Schema(name = "DeviceUnbindDTO", description = "设备解绑请求参数")
public class DeviceUnbindDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备 SN 码
     */
    @NotBlank(message = "设备 SN 码不能为空")
    @Schema(description = "设备 SN 码", example = "DEV20260331001", required = true)
    private String deviceSn;
}