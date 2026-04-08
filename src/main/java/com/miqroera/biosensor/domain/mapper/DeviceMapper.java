package com.miqroera.biosensor.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.miqroera.biosensor.domain.model.Device;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * <p>
 * 设备信息表 Mapper 接口
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 更新设备激活时间（仅在 activated_at 为 NULL 时更新）
     *
     * @param deviceId    设备 ID
     * @param activatedAt 激活时间
     * @return 影响行数
     */
    int updateActivatedAt(@Param("deviceId") Long deviceId, @Param("activatedAt") LocalDateTime activatedAt);

    /**
     * 更新设备使用信息（最后使用时间和累计次数）
     *
     * @param deviceSn   设备 SN 码
     * @param lastUsedAt 最后使用时间
     * @return 影响行数
     */
    int updateDeviceUsage(@Param("deviceSn") String deviceSn, @Param("lastUsedAt") LocalDateTime lastUsedAt);
}
