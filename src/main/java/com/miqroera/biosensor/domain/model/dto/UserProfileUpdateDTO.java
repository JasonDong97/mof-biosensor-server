package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "用户信息更新请求")
public class UserProfileUpdateDTO {

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    @Past(message = "出生日期不能大于今天")
    @Schema(description = "出生日期")
    private LocalDate birthday;

    @Positive(message = "身高必须大于0")
    @Schema(description = "身高(cm)")
    private Float height;

    @Positive(message = "体重必须大于0")
    @Schema(description = "体重(kg)")
    private Float weight;
}