package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统计摘要VO
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SummaryVO", description = "统计摘要")
public class SummaryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 本周平均浓度 (ppb)
     */
    @Schema(description = "本周平均浓度 (ppb)")
    private Double thisWeekAvg;

    /**
     * 上周平均浓度 (ppb)
     */
    @Schema(description = "上周平均浓度 (ppb)")
    private Double lastWeekAvg;

    /**
     * 变化百分比 (%)
     */
    @Schema(description = "变化百分比 (%)")
    private Integer changePercent;

    /**
     * 趋势：up / down / flat
     */
    @Schema(description = "趋势：up, down, flat")
    private String trend;
}
