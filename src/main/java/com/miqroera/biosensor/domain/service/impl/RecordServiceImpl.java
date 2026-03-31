package com.miqroera.biosensor.domain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.RecordMapper;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.service.IRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Record addRecord(Long userId, RecordAddDTO dto) {
        log.info("上报检测记录，userId: {}, recordId: {}", userId, dto.getRecordId());

        // 1. 幂等性检查：record_id 是否已存在
        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getRecordId, dto.getRecordId());
        Record existingRecord = this.getOne(queryWrapper);

        if (existingRecord != null) {
            log.info("记录已存在，返回已有记录，recordId: {}", dto.getRecordId());
            return existingRecord;
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
