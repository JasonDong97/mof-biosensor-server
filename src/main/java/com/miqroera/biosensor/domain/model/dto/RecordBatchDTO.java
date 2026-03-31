package com.miqroera.biosensor.domain.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 批量检测记录上报 DTO
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Data
@Schema(name = "RecordBatchDTO", description = "批量检测记录上报参数")
public class RecordBatchDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 检测记录列表（最多 50 条）
     */
    @NotEmpty(message = "检测记录列表不能为空")
    @Size(max = 50, message = "每次最多上报 50 条记录")
    @Schema(description = "检测记录列表（最多 50 条）", required = true)
    private List<@Valid RecordAddDTO> records;
}