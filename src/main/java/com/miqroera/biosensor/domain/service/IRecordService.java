package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.model.dto.RecordQuery;
import com.miqroera.biosensor.domain.model.vo.RecordDetailVO;
import com.miqroera.biosensor.domain.model.vo.RecordListVO;
import com.miqroera.biosensor.domain.model.vo.SummaryVO;
import com.miqroera.biosensor.domain.model.vo.TrendDataVO;
import com.miqroera.biosensor.infra.domain.model.PageResult;

import java.util.List;

/**
 * <p>
 * 检测记录表 服务类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
public interface IRecordService extends IService<Record> {

    /**
     * 单条上报检测记录
     *
     * @param userId 用户 ID
     * @param dto    上报参数
     * @return 保存后的记录
     */
    Record addRecord(Long userId, RecordAddDTO dto);

    /**
     * 批量上报检测记录
     *
     * @param userId 用户 ID
     * @param dtos   上报参数列表
     * @return 保存后的记录列表
     */
    List<Record> batchAddRecords(Long userId, List<RecordAddDTO> dtos);

    /**
     * 分页查询检测记录
     *
     * @param userId 用户 ID
     * @param query  查询参数
     * @return 分页记录列表
     */
    PageResult<RecordListVO> queryRecords(Long userId, RecordQuery query);

    /**
     * 获取趋势数据（折线图）
     *
     * @param userId   用户 ID
     * @param type     筛选类型：week 按周, day 按天
     * @param deviceSn 设备SN，不传则查用户所有设备
     * @return 趋势数据
     */
    TrendDataVO getTrendData(Long userId, String type, String deviceSn);

    /**
     * 获取统计摘要
     *
     * @param userId   用户 ID
     * @param deviceSn 设备SN，不传则查用户所有设备
     * @return 统计摘要
     */
    SummaryVO getSummary(Long userId, String deviceSn);

    /**
     * 获取记录详情
     *
     * @param userId   用户 ID
     * @param recordId 记录 ID
     * @return 记录详情
     */
    Record getRecordDetail(Long userId, String recordId);

    /**
     * 获取指定设备的最新一次检测记录（关联设备信息）
     *
     * @param userId   用户 ID
     * @param deviceSn 设备 SN
     * @return 最新记录详情
     */
    RecordDetailVO getLatestRecord(Long userId, String deviceSn);

}
