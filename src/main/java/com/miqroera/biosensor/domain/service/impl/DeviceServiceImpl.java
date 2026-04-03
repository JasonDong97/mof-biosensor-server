package com.miqroera.biosensor.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.DeviceMapper;
import com.miqroera.biosensor.domain.model.Device;
import com.miqroera.biosensor.domain.service.IDeviceService;
import com.miqroera.biosensor.domain.service.IUserDeviceService;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备信息表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@RequiredArgsConstructor
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    private final IUserDeviceService userDeviceService;

    @Override
    public Device checkExists(String deviceSn) {
        Device device = this.lambdaQuery().eq(Device::getDeviceSn, deviceSn).one();
        if (device == null) {
            throw ServiceException.of("设备 {} 未绑定，请先绑定设备", deviceSn);
        }
        if ("1".equalsIgnoreCase(device.getStatus())) {
            throw ServiceException.of("设备 {} 已被禁用", deviceSn);
        }
        return device;
    }

    @Override
    public void checkBinded(Long userId, String deviceSn) {
        Device device = checkExists(deviceSn);
        try {
            userDeviceService.checkExists(userId, device.getId());
        } catch (Exception e) {
            throw ServiceException.of("设备未绑定：{}", deviceSn);
        }
    }


}
