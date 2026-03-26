package com.miqroera.biosensor.domain.service.impl;

import com.miqroera.biosensor.domain.model.UserDevice;
import com.miqroera.biosensor.domain.mapper.UserDeviceMapper;
import com.miqroera.biosensor.domain.service.IUserDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户设备绑定关系表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Service
public class UserDeviceServiceImpl extends ServiceImpl<UserDeviceMapper, UserDevice> implements IUserDeviceService {

}
