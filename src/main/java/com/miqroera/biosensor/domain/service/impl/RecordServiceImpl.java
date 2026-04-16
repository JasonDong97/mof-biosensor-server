package com.miqroera.biosensor.domain.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.RecordMapper;
import com.miqroera.biosensor.domain.mapper.SysUserMapper;
import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.model.dto.RecordAddDTO;
import com.miqroera.biosensor.domain.model.dto.RecordQuery;
import com.miqroera.biosensor.domain.model.vo.DailyValueVO;
import com.miqroera.biosensor.domain.model.vo.RecordListVO;
import com.miqroera.biosensor.domain.model.vo.SummaryVO;
import com.miqroera.biosensor.domain.model.vo.TrendDataVO;
import com.miqroera.biosensor.domain.service.IDeviceService;
import com.miqroera.biosensor.domain.service.IRecordService;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.domain.model.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

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
    private final SysUserMapper sysUserMapper;
    private final com.miqroera.biosensor.domain.mapper.DeviceMapper deviceMapper;
    private final RecordMapper recordMapper;

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

        // 4. 原子更新用户首次检测日期和累计次数
        updateUserMeasureInfo(userId, dto.getTimestamp());

        // 5. 更新设备使用信息（最后使用时间和累计次数）
        updateDeviceUsage(deviceSn, dto.getTimestamp());

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
    public PageResult<RecordListVO> queryRecords(Long userId, RecordQuery query) {
        log.info("查询检测记录，userId: {}, 查询参数：{}", userId, query);

        // 1. 构建查询条件
        LambdaQueryChainWrapper<Record> queryWrapper = this.lambdaQuery()
                .eq(Record::getUserId, userId)
                .eq(Record::getDelFlag, "0");

        // 时间范围筛选
        if (query.getStartTime() != null) {
            queryWrapper.ge(Record::getTimestamp, query.getStartTime().atStartOfDay());
        }
        if (query.getEndTime() != null) {
            queryWrapper.le(Record::getTimestamp, query.getEndTime().atTime(LocalTime.MAX));
        }

        Page<Record> pageResult = queryWrapper.eq(query.getSceneType() != null, Record::getSceneType, query.getSceneType())
                .eq(query.getDeviceSn() != null && !query.getDeviceSn().isBlank(), Record::getDeviceSn, query.getDeviceSn())
                .eq(query.getLevel() != null, Record::getLevel, query.getLevel())
                .orderByDesc(Record::getTimestamp)
                .page(new Page<>(query.getPageNum(), query.getPageSize()));

        // 3. 转换为 PageResult
        log.info("查询检测记录成功，userId: {}, 记录数：{}", userId, pageResult.getRecords().size());
        return PageResult.build(pageResult, RecordListVO.class);
    }

    @Override
    public TrendDataVO getTrendData(Long userId, String type, String deviceSn) {
        log.info("获取趋势数据，userId: {}, type: {}, deviceSn: {}", userId, type, deviceSn);

        // 计算最近14天的日期范围
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(13);
        LocalDate endDate = today.plusDays(1);

        // 查询空腹数据 (sceneType=1)
        List<DailyValueVO> fastingData = getDailyAverages(userId, deviceSn, 1, startDate, endDate);

        // 查询餐后数据 (sceneType=2)
        List<DailyValueVO> postmealData = getDailyAverages(userId, deviceSn, 2, startDate, endDate);

        // 查询运动后数据 (sceneType=3)
        List<DailyValueVO> afterExerciseData = getDailyAverages(userId, deviceSn, 3, startDate, endDate);

        return TrendDataVO.builder()
                .fasting(fastingData)
                .postmeal(postmealData)
                .afterExercise(afterExerciseData)
                .build();
    }

    @Override
    public SummaryVO getSummary(Long userId, String deviceSn) {
        log.info("获取统计摘要，userId: {}, deviceSn: {}", userId, deviceSn);

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastWeekStart = weekStart.minusWeeks(1);

        // 本周平均
        double thisWeekAvg = getAverageConcentration(userId, deviceSn, lastWeekStart, weekStart);

        // 上周平均
        double lastWeekAvg = getAverageConcentration(userId, deviceSn, lastWeekStart.minusWeeks(1), lastWeekStart);

        // 计算变化百分比
        int changePercent = 0;
        String trend = "flat";
        if (lastWeekAvg > 0) {
            changePercent = BigDecimal.valueOf((thisWeekAvg - lastWeekAvg) / lastWeekAvg * 100)
                    .setScale(0, RoundingMode.HALF_UP).intValue();
            if (changePercent > 0) {
                trend = "up";
            } else if (changePercent < 0) {
                trend = "down";
            }
        }

        return SummaryVO.builder()
                .thisWeekAvg((double) Math.round(thisWeekAvg))
                .lastWeekAvg((double) Math.round(lastWeekAvg))
                .changePercent(changePercent)
                .trend(trend)
                .build();
    }

    @Override
    public Record getRecordDetail(Long userId, String recordId) {
        return this.lambdaQuery().eq(Record::getUserId, userId)
                .eq(Record::getId, recordId)
                .one();
    }

    /**
     * 获取指定日期范围内每天的平均浓度（按场景类型筛选）
     *
     * @param userId     用户ID
     * @param deviceSn   设备序列号（可选）
     * @param sceneType  场景类型（1-空腹 2-餐后 3-运动后）
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 每日平均浓度列表
     */
    private List<DailyValueVO> getDailyAverages(Long userId, String deviceSn, Integer sceneType, LocalDate startDate, LocalDate endDate) {
        List<Record> records = getAverage(userId, deviceSn, sceneType, startDate, endDate);

        // 按日期分组计算平均值
        Map<LocalDate, List<Record>> byDate = records.stream()
                .collect(Collectors.groupingBy(r -> r.getTimestamp().toLocalDate()));

        List<DailyValueVO> result = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            List<Record> dayRecords = byDate.get(date);
            Double avg = null;
            if (dayRecords != null && !dayRecords.isEmpty()) {
                OptionalDouble optAvg = dayRecords.stream()
                        .map(Record::getConcentration)
                        .filter(Objects::nonNull)
                        .mapToDouble(Double::doubleValue)
                        .average();
                avg = optAvg.isPresent() ? optAvg.getAsDouble() : null;
            }
            final Double finalAvg = avg;
            result.add(DailyValueVO.builder()
                    .date(date.toString())
                    .day(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.CHINA))
                    .value(finalAvg != null ? (double) Math.round(finalAvg) : 0.0)
                    .build());
        }

        return result;
    }

    /**
     * 查询指定日期范围内的检测记录
     *
     * @param userId     用户ID
     * @param deviceSn   设备序列号（可选）
     * @param sceneType  场景类型（可选，1-空腹 2-餐后 3-运动后）
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 符合条件的检测记录列表
     */
    private List<Record> getAverage(Long userId, String deviceSn, Integer sceneType, LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        LambdaQueryWrapper<Record> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Record::getUserId, userId)
                .eq(Record::getDelFlag, "0")
                .ge(Record::getTimestamp, start)
                .lt(Record::getTimestamp, end)
                .isNotNull(Record::getConcentration);

        if (deviceSn != null && !deviceSn.isBlank()) {
            queryWrapper.eq(Record::getDeviceSn, deviceSn);
        }
        if (sceneType != null) {
            queryWrapper.eq(Record::getSceneType, sceneType);
        }

        return this.list(queryWrapper);
    }

    /**
     * 获取指定日期范围内的平均浓度
     *
     * @param userId     用户ID
     * @param deviceSn   设备序列号（可选）
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return 平均浓度值
     */
    private Double getAverageConcentration(Long userId, String deviceSn, LocalDate startDate, LocalDate endDate) {
        List<Record> records = getAverage(userId, deviceSn, null, startDate, endDate);
        if (records.isEmpty()) {
            return 0.0;
        }

        return records.stream()
                .map(Record::getConcentration)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
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
     * 原子更新用户检测信息（首次检测日期和累计次数）
     */
    private void updateUserMeasureInfo(Long userId, LocalDateTime measureTime) {
        sysUserMapper.updateUserMeasureInfo(userId, measureTime.toLocalDate());
        log.debug("用户检测信息更新成功，userId: {}, measureTime: {}", userId, measureTime);
    }

    /**
     * 更新设备使用信息（最后使用时间和累计次数）
     */
    private void updateDeviceUsage(String deviceSn, LocalDateTime useTime) {
        deviceMapper.updateDeviceUsage(deviceSn, useTime);
        log.debug("设备使用信息更新成功，deviceSn: {}, useTime: {}", deviceSn, useTime);
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
