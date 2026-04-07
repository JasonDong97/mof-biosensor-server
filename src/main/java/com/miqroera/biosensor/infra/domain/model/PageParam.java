package com.miqroera.biosensor.infra.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;

/**
 * @author wangbin
 * @version 1.0
 * @date 2023/7/3 15:28
 * @description 分页参数
 */
@Data
@NoArgsConstructor
@SuppressWarnings("ALL")
@Slf4j
public class PageParam {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 不适用分页参数
     * pageSize < 0 时，不分页
     */
    public static PageParam NO_PAGE = new PageParam(1, -1, true);
    @Schema(description = "页面编号", required = true, example = "1")
    private Integer pageNum = 1;

    @Schema(description = "页面大小", required = true, example = "10")
    private Integer pageSize = 10;


    public PageParam(int pageNum, int pageSize) {
        this(pageNum, pageSize, false);
    }

    public PageParam(int pageNum, int pageSize, Boolean inner) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

}
