package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.UserDevice;
import com.miqroera.biosensor.domain.model.dto.DeviceAddDTO;
import com.miqroera.biosensor.domain.model.dto.DeviceBindDTO;
import com.miqroera.biosensor.domain.model.dto.DeviceUnbindDTO;
import com.miqroera.biosensor.domain.model.vo.DeviceListVO;
import com.miqroera.biosensor.domain.model.vo.DeviceVO;

import java.util.List;

/**
 * <p>
 * 用户设备绑定关系表 服务类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
public interface IUserDeviceService extends IService<UserDevice> {

    /**
     * 绑定设备
     *
     * @param userId 用户 ID
     * @param dto    绑定请求参数
     * @return 设备信息
     */
    DeviceVO bindDevice(Long userId, DeviceBindDTO dto);

    /**
     * 解绑设备
     *
     * @param userId 用户 ID
     * @param dto    解绑请求参数
     */
    void unbindDevice(Long userId, DeviceUnbindDTO dto);

    /**
     * 获取用户设备列表
     *
     * @param userId 用户 ID
     * @return 设备列表
     */
    List<DeviceListVO> getUserDeviceList(Long userId);

    /**
     * 获取设备详情
     *
     * @param userId    用户 ID
     * @param deviceSn  设备 SN
     * @return 设备详情
     */
    DeviceVO getDeviceDetail(Long userId, String deviceSn);

    /**
     * 检查设备是否存在
     *
     * @param userId   用户 ID
     * @param deviceId 设备 ID
     */
    void checkExists(Long userId, Long deviceId);

    /**
     * 管理员添加设备
     *
     * @param dto 设备添加请求参数
     * @return 设备信息
     */
    DeviceVO addDevice(DeviceAddDTO dto);
}
