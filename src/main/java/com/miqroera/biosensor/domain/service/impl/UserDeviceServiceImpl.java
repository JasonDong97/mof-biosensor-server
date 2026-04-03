package com.miqroera.biosensor.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.DeviceMapper;
import com.miqroera.biosensor.domain.mapper.UserDeviceMapper;
import com.miqroera.biosensor.domain.model.Device;
import com.miqroera.biosensor.domain.model.UserDevice;
import com.miqroera.biosensor.domain.model.dto.DeviceBindDTO;
import com.miqroera.biosensor.domain.model.dto.DeviceUnbindDTO;
import com.miqroera.biosensor.domain.model.vo.DeviceListVO;
import com.miqroera.biosensor.domain.model.vo.DeviceVO;
import com.miqroera.biosensor.domain.service.IUserDeviceService;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户设备绑定关系表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements IUserDeviceService {

    private final DeviceMapper deviceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeviceVO bindDevice(Long userId, DeviceBindDTO dto) {
        log.info("绑定设备，userId: {}, deviceSn: {}", userId, dto.getDeviceSn());

        // 1. 查询设备是否存在
        LambdaQueryWrapper<Device> deviceQuery = new LambdaQueryWrapper<>();
        deviceQuery.eq(Device::getDeviceSn, dto.getDeviceSn());
        Device device = deviceMapper.selectOne(deviceQuery);

        if (device == null) {
            // 设备不存在，自动注册新设备
            device = new Device();
            device.setDeviceSn(dto.getDeviceSn());
            device.setStatus("0"); // 正常状态
            device.setActivatedAt(LocalDateTime.now()); // 激活时间为首次绑定时间
            deviceMapper.insert(device);
            log.info("自动注册新设备，deviceId: {}, deviceSn: {}", device.getId(), dto.getDeviceSn());
        }

        // 2. 检查设备状态
        if ("1".equals(device.getStatus())) {
            throw new ServiceException("设备已被禁用");
        }

        // 3. 检查是否已被其他用户绑定
        LambdaQueryWrapper<UserDevice> bindQuery = new LambdaQueryWrapper<>();
        bindQuery.eq(UserDevice::getDeviceId, device.getId());
        bindQuery.eq(UserDevice::getIsActive, (byte) 1);
        UserDevice existingBind = this.getOne(bindQuery);

        if (existingBind != null) {
            if (!existingBind.getUserId().equals(userId)) {
                throw new ServiceException("设备已被其他用户绑定");
            }
            // 同一用户重复绑定，直接返回设备信息
            return buildDeviceVO(device, existingBind.getBindTime());
        }

        // 4. 创建绑定关系
        UserDevice userDevice = new UserDevice();
        userDevice.setUserId(userId);
        userDevice.setDeviceId(device.getId());
        userDevice.setBindTime(LocalDateTime.now());
        userDevice.setIsActive((byte) 1);
        this.save(userDevice);

        // 5. 更新设备激活时间和使用次数
        if (device.getActivatedAt() == null) {
            device.setActivatedAt(LocalDateTime.now());
        }
        deviceMapper.updateById(device);

        log.info("设备绑定成功，userId: {}, deviceSn: {}", userId, dto.getDeviceSn());
        return buildDeviceVO(device, userDevice.getBindTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindDevice(Long userId, DeviceUnbindDTO dto) {
        log.info("解绑设备，userId: {}, deviceSn: {}", userId, dto.getDeviceSn());

        // 1. 查询设备
        LambdaQueryWrapper<Device> deviceQuery = new LambdaQueryWrapper<>();
        deviceQuery.eq(Device::getDeviceSn, dto.getDeviceSn());
        Device device = deviceMapper.selectOne(deviceQuery);

        if (device == null) {
            throw new ServiceException("设备不存在");
        }

        // 2. 查询绑定关系
        LambdaQueryWrapper<UserDevice> bindQuery = new LambdaQueryWrapper<>();
        bindQuery.eq(UserDevice::getUserId, userId);
        bindQuery.eq(UserDevice::getDeviceId, device.getId());
        bindQuery.eq(UserDevice::getIsActive, (byte) 1);
        UserDevice userDevice = this.getOne(bindQuery);

        if (userDevice == null) {
            throw new ServiceException("未绑定该设备");
        }

        // 3. 更新绑定关系（软删除）
        userDevice.setUnbindTime(LocalDateTime.now());
        userDevice.setIsActive((byte) 0);
        this.updateById(userDevice);

        log.info("设备解绑成功，userId: {}, deviceSn: {}", userId, dto.getDeviceSn());
    }

    @Override
    public List<DeviceListVO> getUserDeviceList(Long userId) {
        log.info("获取用户设备列表，userId: {}", userId);

        // 1. 查询用户绑定的设备（只查询当前绑定的）
        LambdaQueryWrapper<UserDevice> bindQuery = new LambdaQueryWrapper<>();
        bindQuery.eq(UserDevice::getUserId, userId);
        bindQuery.eq(UserDevice::getIsActive, (byte) 1);
        bindQuery.orderByDesc(UserDevice::getBindTime);
        List<UserDevice> userDevices = this.list(bindQuery);

        if (userDevices.isEmpty()) {
            return List.of();
        }

        // 2. 获取设备 ID 列表
        List<Long> deviceIds = userDevices.stream()
                .map(UserDevice::getDeviceId)
                .collect(Collectors.toList());

        // 3. 查询设备信息
        LambdaQueryWrapper<Device> deviceQuery = new LambdaQueryWrapper<>();
        deviceQuery.in(Device::getId, deviceIds);
        List<Device> devices = deviceMapper.selectList(deviceQuery);

        // 4. 构建设备列表 VO
        return devices.stream()
                .map(device -> {
                    UserDevice userDevice = userDevices.stream()
                            .filter(ud -> ud.getDeviceId().equals(device.getId()))
                            .findFirst()
                            .orElse(null);
                    return DeviceListVO.builder()
                            .id(device.getId())
                            .deviceSn(device.getDeviceSn())
                            .mac(device.getMac())
                            .firmwareVersion(device.getFirmwareVersion())
                            .hardwareVersion(device.getHardwareVersion())
                            .lastUsedAt(device.getLastUsedAt())
                            .totalUses(device.getTotalUses())
                            .bindTime(userDevice != null ? userDevice.getBindTime() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public DeviceVO getDeviceDetail(Long userId, String deviceSn) {
        log.info("获取设备详情，userId: {}, deviceSn: {}", userId, deviceSn);

        // 1. 查询设备
        LambdaQueryWrapper<Device> deviceQuery = new LambdaQueryWrapper<>();
        deviceQuery.eq(Device::getDeviceSn, deviceSn);
        Device device = deviceMapper.selectOne(deviceQuery);

        if (device == null) {
            throw new ServiceException("设备不存在");
        }

        // 2. 查询绑定关系
        LambdaQueryWrapper<UserDevice> bindQuery = new LambdaQueryWrapper<>();
        bindQuery.eq(UserDevice::getUserId, userId);
        bindQuery.eq(UserDevice::getDeviceId, device.getId());
        UserDevice userDevice = this.getOne(bindQuery);

        if (userDevice == null) {
            throw new ServiceException("未绑定该设备");
        }

        return buildDeviceVO(device, userDevice.getBindTime());
    }

    @Override
    public void checkExists(Long userId, Long deviceId) {
        boolean exists = this.lambdaQuery().eq(UserDevice::getUserId, userId)
                .eq(UserDevice::getDeviceId, deviceId)
                .eq(UserDevice::getIsActive, 1)
                .exists();
        if (!exists) {
            throw new ServiceException("设备未绑定：{}", deviceId);
        }
    }

    /**
     * 构建 DeviceVO
     */
    private DeviceVO buildDeviceVO(Device device, LocalDateTime bindTime) {
        return DeviceVO.builder()
                .id(device.getId())
                .deviceSn(device.getDeviceSn())
                .mac(device.getMac())
                .firmwareVersion(device.getFirmwareVersion())
                .hardwareVersion(device.getHardwareVersion())
                .batchNo(device.getBatchNo())
                .activatedAt(device.getActivatedAt())
                .lastUsedAt(device.getLastUsedAt())
                .totalUses(device.getTotalUses())
                .status(device.getStatus())
                .bindTime(bindTime)
                .build();
    }
}
