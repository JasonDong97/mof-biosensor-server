package com.miqroera.biosensor.domain.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.DeviceMapper;
import com.miqroera.biosensor.domain.mapper.SysUserMapper;
import com.miqroera.biosensor.domain.model.Device;
import com.miqroera.biosensor.domain.model.UserDevice;
import com.miqroera.biosensor.domain.model.vo.DeviceVO;
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
    private final SysUserMapper userMapper;

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

    @Override
    public DeviceVO getBindDeviceDetail(Long deviceId) {

        Device device = this.getById(deviceId);
        if (device == null) {
            return null;
        }

        return convertVO(device, null);


    }

    private DeviceVO convertVO(Device device, Long userId) {
        DeviceVO vo = BeanUtil.toBean(device, DeviceVO.class);
        UserDevice bindInfo = userDeviceService.lambdaQuery()
                .eq(userId != null, UserDevice::getUserId, userId)
                .eq(UserDevice::getDeviceId, device.getId())
                .eq(UserDevice::getIsActive, 1)
                .list().stream().findFirst().orElse(null);
        if (bindInfo != null) {
            vo.setBindUserId(bindInfo.getUserId());
            vo.setBindTime(bindInfo.getBindTime());
            vo.setBindUserNickName(userMapper.selectNickNameById(bindInfo.getUserId()));
        }
        return vo;
    }

    @Override
    public DeviceVO getBindDeviceDetail(Long userId, String deviceSn) {
        Device device = this.lambdaQuery().eq(Device::getDeviceSn, deviceSn)
                .eq(Device::getStatus, "0")
                .one();

        if (device == null) {
            return null;
        }

        DeviceVO vo = convertVO(device, userId);
        if (vo.getBindUserId() == null) {
            throw ServiceException.of("您与该设备（{}）未绑定，无法查看详情", deviceSn);
        }

        return vo;
    }


}
