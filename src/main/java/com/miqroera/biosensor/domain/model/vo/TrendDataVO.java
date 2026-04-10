package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 趋势数据VO
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "TrendDataVO", description = "趋势数据")
public class TrendDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 本周数据
     */
    @Schema(description = "本周数据")
    private List<DailyValueVO> thisWeek;

    /**
     * 上周数据
     */
    @Schema(description = "上周数据")
    private List<DailyValueVO> lastWeek;
}
