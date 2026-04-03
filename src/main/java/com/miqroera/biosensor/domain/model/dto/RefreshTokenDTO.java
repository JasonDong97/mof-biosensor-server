package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenDTO {

    @Schema(description = "刷新 Token")
    private String refreshToken;
}
