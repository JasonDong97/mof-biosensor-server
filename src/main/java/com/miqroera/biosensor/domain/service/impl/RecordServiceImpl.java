package com.miqroera.biosensor.domain.service.impl;

import com.miqroera.biosensor.domain.model.Record;
import com.miqroera.biosensor.domain.mapper.RecordMapper;
import com.miqroera.biosensor.domain.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 检测记录表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

}
