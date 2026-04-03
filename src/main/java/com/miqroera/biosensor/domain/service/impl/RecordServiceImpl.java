package com.miqroera.biosensor.domain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.RecordMapper;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.model.dto.RecordQuery;
import com.miqroera.biosensor.domain.model.vo.RecordListVO;
import com.miqroera.biosensor.domain.service.IDeviceService;
import com.miqroera.biosensor.domain.service.IRecordService;
import com.miqroera.biosensor.domain.service.IUserDeviceService;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 检测记录表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    private final IDeviceService deviceService;
    private final IUserDeviceService userDeviceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Record addRecord(Long userId, RecordAddDTO dto) {
        log.info("上报检测记录，userId: {}, recordId: {}", userId, dto.getRecordId());
        String deviceSn = dto.getDeviceSn();
        // 检查设备是否绑定
        deviceService.checkBinded(userId, deviceSn);

        // 1. 幂等性检查：record_id 是否已存在
        Record existingRecord = this.lambdaQuery()
                .eq(Record::getRecordId, dto.getRecordId())
                .eq(Record::getDeviceSn, dto.getDeviceSn())
                .one();

        if (existingRecord != null) {
            throw ServiceException.of("上报失败，重复记录：{}", dto.getRecordId());
        }

        // 2. 构建记录对象
        Record record = buildRecord(dto, userId);

        // 3. 保存记录
        this.save(record);

        log.info("检测记录上报成功，userId: {}, recordId: {}", userId, dto.getRecordId());
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Record> batchAddRecords(Long userId, List<RecordAddDTO> dtos) {
        log.info("批量上报检测记录，userId: {}, 记录数：{}", userId, dtos.size());

        // 1. 过滤已存在的记录（幂等性检查）
        List<Record> recordsToAdd = dtos.stream()
                .filter(dto -> {
                    LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Record::getRecordId, dto.getRecordId());
                    return this.getOne(queryWrapper) == null;
                })
                .map(dto -> buildRecord(dto, userId))
                .toList();

        if (recordsToAdd.isEmpty()) {
            log.info("所有记录已存在，无需新增，userId: {}", userId);
            return List.of();
        }

        // 2. 批量保存记录
        this.saveBatch(recordsToAdd);

        log.info("批量检测记录上报成功，userId: {}, 新增记录数：{}", userId, recordsToAdd.size());
        return recordsToAdd;
    }

    @Override
    public Page<RecordListVO> queryRecords(Long userId, RecordQuery query) {
        log.info("查询检测记录，userId: {}, 查询参数：{}", userId, query);

        // 1. 构建查询条件
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getUserId, userId)
                .eq(Record::getDelFlag, "0");

        // 时间范围筛选
        if (query.getStartTime() != null) {
            queryWrapper.ge(Record::getTimestamp, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            queryWrapper.le(Record::getTimestamp, query.getEndTime());
        }

        // 场景类型筛选
        if (query.getSceneType() != null) {
            queryWrapper.eq(Record::getSceneType, query.getSceneType());
        }

        // 设备 SN 筛选
        if (query.getDeviceSn() != null && !query.getDeviceSn().isBlank()) {
            queryWrapper.eq(Record::getDeviceSn, query.getDeviceSn());
        }

        // 等级筛选
        if (query.getLevel() != null) {
            queryWrapper.eq(Record::getLevel, query.getLevel());
        }

        // 按检测时间倒序
        queryWrapper.orderByDesc(Record::getTimestamp);

        // 2. 分页查询
        Page<Record> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<Record> resultPage = this.page(page, queryWrapper);

        // 3. 转换为 VO
        List<RecordListVO> voList = CollectionUtils.isEmpty(resultPage.getRecords()) ? List.of()
                : resultPage.getRecords().stream()
                .map(this::convertToVO)
                .toList();

        Page<RecordListVO> voPage = new Page<>(query.getPageNum(), query.getPageSize(), resultPage.getTotal());
        voPage.setRecords(voList);

        log.info("查询检测记录成功，userId: {}, 记录数：{}", userId, voList.size());
        return voPage;
    }

    /**
     * 转换为 VO
     */
    private RecordListVO convertToVO(Record record) {
        return RecordListVO.builder()
                .id(record.getId())
                .recordId(record.getRecordId())
                .deviceSn(record.getDeviceSn())
                .timestamp(record.getTimestamp())
                .sceneType(record.getSceneType())
                .concentration(record.getConcentration())
                .level(record.getLevel())
                .levelLabel(record.getLevelLabel())
                .suggestion(record.getSuggestion())
                .createTime(record.getCreateTime())
                .build();
    }

    /**
     * 构建 Record 对象
     */
    private Record buildRecord(RecordAddDTO dto, Long userId) {
        Record record = new Record();
        record.setRecordId(dto.getRecordId());
        record.setUserId(userId);
        record.setDeviceSn(dto.getDeviceSn());
        record.setTimestamp(dto.getTimestamp());
        record.setSceneType(dto.getSceneType());
        record.setConcentration(dto.getConcentration());
        record.setLevel(dto.getLevel());
        record.setLevelLabel(dto.getLevelLabel());
        record.setSuggestion(dto.getSuggestion());
        record.setRBase(dto.getRBase());
        record.setRGas(dto.getRGas());
        record.setAdcValue(dto.getAdcValue());
        record.setTemperature(dto.getTemperature());
        record.setHumidity(dto.getHumidity());
        record.setHeaterTemp(dto.getHeaterTemp());
        record.setAlgoVersion(dto.getAlgoVersion());
        record.setFirmwareVersion(dto.getFirmwareVersion());
        record.setGasType(dto.getGasType());
        record.setExtraData(dto.getExtraData());
        record.setDelFlag("0");
        record.setCreateBy(StpUtil.getLoginIdAsString());
        return record;
    }
}
