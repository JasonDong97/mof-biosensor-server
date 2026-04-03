package com.miqroera.biosensor.web;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.model.dto.RecordBatchDTO;
import com.miqroera.biosensor.domain.model.dto.RecordQuery;
import com.miqroera.biosensor.domain.model.vo.RecordListVO;
import com.miqroera.biosensor.domain.service.IRecordService;
import com.miqroera.biosensor.infra.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "检测记录管理", description = "检测记录上报、查询")
@ApiSort(value = 4)
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
    public R<Page<RecordListVO>> queryRecords(RecordQuery query) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<RecordListVO> page = recordService.queryRecords(userId, query);
        return R.ok(page);
    }
}
