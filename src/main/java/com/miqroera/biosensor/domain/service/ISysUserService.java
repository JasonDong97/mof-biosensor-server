package com.miqroera.biosensor.domain.service;

import com.miqroera.biosensor.domain.model.SysUser;
import com.miqroera.biosensor.domain.model.dto.PhoneLoginDTO;
import com.miqroera.biosensor.domain.model.dto.WxLoginDTO;
import com.miqroera.biosensor.domain.model.vo.AuthResponseVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 微信小程序登录
     *
     * @param dto 登录参数
     * @return 认证响应（Token + 用户信息）
     */
    AuthResponseVO wxLogin(WxLoginDTO dto);

    /**
     * 手机号登录
     *
     * @param dto 登录参数
     * @return 认证响应（Token + 用户信息）
     */
    AuthResponseVO phoneLogin(PhoneLoginDTO dto);

    /**
     * 刷新 Token
     *
     * @param refreshToken 刷新令牌
     * @return 新的 Token 信息
     */
    AuthResponseVO refreshToken(String refreshToken);

    /**
     * 退出登录
     */
    void logout();
}
