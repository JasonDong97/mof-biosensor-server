package com.miqroera.biosensor.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.miqroera.biosensor.domain.model.Device;
import com.miqroera.biosensor.domain.model.dto.DevicePageQuery;
import com.miqroera.biosensor.domain.model.vo.DeviceVO;
import com.miqroera.biosensor.infra.domain.model.PageResult;

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

    /**
     * 获取设备详情
     *
     * @param deviceId 设备 ID
     */

    DeviceVO getBindDeviceDetail(Long deviceId);

    /**
     * 获取设备详情
     *
     * @param deviceSn 设备 SN
     */
    DeviceVO getBindDeviceDetail(Long userId, String deviceSn);


    /**
     * 获取设备分页列表
     *
     * @param query 查询参数
     */
    PageResult<DeviceVO> getDevicePage(DevicePageQuery query);
}
