package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SmsSendDTO {

    @NotNull
    @Schema(description = "手机号", required = true)
    private String phoneNumber;
}
