package com.miqroera.biosensor.infra.domain.model;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author wangbin
 * @version 1.0
 * @date 2022/11/26 21:27
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuppressWarnings("ALL")
public class PageResult<T> extends PageParam {

    /**
     * 分页数据对象
     */
    @Schema(description = "列表数据")
    private List<T> rows;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数", example = "10")
    private long total;

    public PageResult() {
    }

    public <V> PageResult(IPage<V> page, List<T> records) {
        Assert.notNull(page, "分页对象不能为空");
        this.setPageNum((int) page.getCurrent());
        this.setPageSize((int) page.getSize());
        this.setTotal(page.getTotal());
        this.setRows(Optional.of(records).orElse(Collections.emptyList()));
    }

    public <V> PageResult(IPage<V> page, Class<T> clazz) {
        Assert.notNull(page, "分页对象不能为空");
        this.setPageNum((int) page.getCurrent());
        this.setPageSize((int) page.getSize());
        this.setTotal(page.getTotal());
        this.setRows(Optional.of(BeanUtil.copyToList(page.getRecords(), clazz)).orElse(Collections.emptyList()));
    }


    /**
     * 分页后的数据库数据转换为PageResult
     *
     * @return 分页结果
     */
    public static <T> PageResult<T> build(IPage page, List<T> records) {
        return new PageResult<T>(page, records);
    }

    public static <T> PageResult<T> build(PageParam pageParam) {
        PageResult<T> r = new PageResult<>();
        r.setRows(Collections.emptyList());
        r.setTotal(0);
        r.setPageNum(pageParam.getPageNum());
        r.setPageSize(pageParam.getPageSize());
        return r;
    }


    /**
     * 分页后的数据库数据转换为PageResult
     *
     * @param page 数据库分页结果
     * @param clz  分页数据需要转换成的类 一般为VO  ,SysUser -> UserVO
     * @return 分页结果
     */
    public static <T> PageResult<T> build(IPage page, Class<T> clz) {
        return new PageResult<T>(page, clz);
    }


    /**
     * 分页后的VO Page对象转换为PageResult
     *
     * @param page 数据库分页结果
     * @return 分页结果
     */
    public static <T> PageResult<T> build(IPage page) {
        return new PageResult<T>(page, page.getRecords());
    }


    public static <T> PageResult<T> empty(PageParam page) {
        PageResult<T> res = new PageResult<>();
        res.setRows(Collections.emptyList());
        res.setPageNum(page.getPageNum());
        res.setPageSize(page.getPageSize());
        return res;
    }


    public static <T> PageResult<T> empty(Page<T> page) {
        PageResult<T> res = new PageResult<>();
        res.setRows(Collections.emptyList());
        res.setPageNum((int) page.getCurrent());
        res.setPageSize((int) page.getSize());
        return res;
    }

}
