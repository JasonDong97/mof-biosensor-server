package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SmsCodeVerifyDTO {

    @Schema(description = "手机号", required = true)
    private String phoneNumber;

    @Schema(description = "验证码",required = true)
    private String code;
}
