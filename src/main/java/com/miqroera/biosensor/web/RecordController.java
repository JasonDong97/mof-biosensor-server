package com.miqroera.biosensor.web;

import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.model.dto.RecordBatchDTO;
import com.miqroera.biosensor.domain.model.dto.RecordQuery;
import com.miqroera.biosensor.domain.model.vo.RecordListVO;
import com.miqroera.biosensor.domain.model.vo.SummaryVO;
import com.miqroera.biosensor.domain.model.vo.TrendDataVO;
import com.miqroera.biosensor.domain.service.IRecordService;
import com.miqroera.biosensor.infra.domain.model.PageResult;
import com.miqroera.biosensor.infra.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检测记录控制器
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Slf4j
@Tag(name = "检测记录", description = "检测记录上报、查询")
@ApiSupport(order = 5)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/records")
public class RecordController {

    private final IRecordService recordService;

    /**
     * 单条检测记录上报
     */
    @PostMapping
    @Operation(summary = "单条检测记录上报", description = "上报单条检测记录到服务器")
    public R<Record> addRecord(@Valid @RequestBody RecordAddDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        Record record = recordService.addRecord(userId, dto);
        return R.ok(record);
    }

    @GetMapping("/{recordId}")
    @Operation(summary = "获取检测记录详情", description = "获取指定检测记录的详细信息")
    public R<Record> getRecordDetail(
            @Parameter(description = "检测记录 ID")
            @PathVariable String recordId) {
        Long userId = StpUtil.getLoginIdAsLong();
        Record record = recordService.getRecordDetail(userId, recordId);
        return R.ok(record);
    }

    /**
     * 批量检测记录上报
     */
    @PostMapping("/batch")
    @Operation(summary = "批量检测记录上报", description = "批量上报检测记录到服务器（每次最多 50 条）")
    public R<List<Record>> batchAddRecords(@Valid @RequestBody RecordBatchDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Record> records = recordService.batchAddRecords(userId, dto.getRecords());
        return R.ok(records);
    }

    /**
     * 分页查询检测记录
     */
    @GetMapping
    @Operation(summary = "分页查询检测记录", description = "分页查询当前用户的检测记录，支持时间、场景、设备筛选")
    public R<PageResult<RecordListVO>> queryRecords(RecordQuery query) {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(recordService.queryRecords(userId, query));
    }

    /**
     * 获取趋势数据（折线图）
     */
    @GetMapping("/stats/trend")
    @Operation(summary = "获取趋势数据", description = "获取本周与上周的每日浓度数据对比")
    public R<TrendDataVO> getTrendData(
            @RequestParam(defaultValue = "week") String type,
            @RequestParam(required = false) String deviceSn) {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(recordService.getTrendData(userId, type, deviceSn));
    }

    /**
     * 获取统计摘要
     */
    @GetMapping("/stats/summary")
    @Operation(summary = "获取统计摘要", description = "获取本周平均浓度及对比上周变化")
    public R<SummaryVO> getSummary(@RequestParam(required = false) String deviceSn) {
        Long userId = StpUtil.getLoginIdAsLong();
        return R.ok(recordService.getSummary(userId, deviceSn));
    }
}
