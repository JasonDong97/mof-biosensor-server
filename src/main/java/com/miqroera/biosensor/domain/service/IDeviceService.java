package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.Device;

/**
 * <p>
 * 设备信息表 服务类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
public interface IDeviceService extends IService<Device> {

    /**
     * 检查设备是否存在
     *
     * @param deviceSn 设备 SN
     */
    Device checkExists(String deviceSn);

    /**
     * 检查设备是否已绑定
     *
     * @param userId   用户 ID
     * @param deviceSn 设备 SN
     */
    void checkBinded(Long userId, String deviceSn);
}
