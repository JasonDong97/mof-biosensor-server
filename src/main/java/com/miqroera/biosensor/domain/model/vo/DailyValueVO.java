package com.miqroera.biosensor.domain.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 每日数据VO
 *
 * @author dongjingxiang
 * @since 2026-04-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "DailyValueVO", description = "每日数据")
public class DailyValueVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 日期（yyyy-MM-dd）
     */
    @Schema(description = "日期")
    private String date;

    /**
     * 星期几（周一、周二...）
     */
    @Schema(description = "星期")
    private String day;

    /**
     * 平均浓度值 (ppb)
     */
    @Schema(description = "平均浓度值 (ppb)")
    private Double value;
}
