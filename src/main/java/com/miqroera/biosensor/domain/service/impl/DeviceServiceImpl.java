package com.miqroera.biosensor.domain.service.impl;

import com.miqroera.biosensor.domain.model.Device;
import com.miqroera.biosensor.domain.mapper.DeviceMapper;
import com.miqroera.biosensor.domain.service.IDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备信息表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

}
