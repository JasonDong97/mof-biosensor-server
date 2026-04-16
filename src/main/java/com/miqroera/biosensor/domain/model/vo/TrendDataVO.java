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
     * 空腹数据（近14天）
     */
    @Schema(description = "空腹数据（近14天）")
    private List<DailyValueVO> fasting;

    /**
     * 餐后数据（近14天）
     */
    @Schema(description = "餐后数据（近14天）")
    private List<DailyValueVO> postmeal;

    /**
     * 运动后数据（近14天）
     */
    @Schema(description = "运动后数据（近14天）")
    private List<DailyValueVO> afterExercise;
}