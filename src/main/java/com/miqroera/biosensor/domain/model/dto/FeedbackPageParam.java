package com.miqroera.biosensor.domain.model.dto;

import com.miqroera.biosensor.infra.domain.model.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackPageParam extends PageParam {

    @Schema(description = "反馈状态, 0 已收到，1 跟进中，2 已完结，不传代表查询所有", example = "0")
    private String status;


}
