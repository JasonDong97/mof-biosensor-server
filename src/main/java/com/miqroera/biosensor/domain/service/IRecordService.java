package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.model.dto.RecordQuery;
import com.miqroera.biosensor.domain.model.vo.RecordListVO;
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
}
