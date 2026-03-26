package com.miqroera.biosensor.domain.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.domain.mapper.SysUserMapper;
import com.miqroera.biosensor.domain.model.SysUser;
import com.miqroera.biosensor.domain.model.dto.PhoneLoginDTO;
import com.miqroera.biosensor.domain.model.dto.WxLoginDTO;
import com.miqroera.biosensor.domain.model.vo.AuthResponseVO;
import com.miqroera.biosensor.domain.model.vo.TokenVO;
import com.miqroera.biosensor.domain.model.vo.UserInfoVO;
import com.miqroera.biosensor.domain.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    /**
     * Token 过期时间（秒）- 7 天
     */
    private static final long TOKEN_TIMEOUT = 7 * 24 * 60 * 60;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponseVO wxLogin(WxLoginDTO dto) {
        log.info("微信小程序登录，code: {}", dto.getCode());

        // TODO: 调用微信接口，通过 code 获取 openid
        // 这里暂时使用 mock 数据，实际开发需要替换为真实的微信 API 调用
        String openid = mockGetWxOpenid(dto.getCode());

        // 查询或创建用户
        SysUser user = getOrCreateUserByOpenid(openid, dto);

        // 生成 Token
        return buildAuthResponse(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponseVO phoneLogin(PhoneLoginDTO dto) {
        log.info("手机号登录，phone: {}", dto.getPhone());

        // TODO: 验证短信验证码
        // 这里暂时使用 mock 数据，实际开发需要替换为真实的短信验证逻辑
        boolean valid = mockVerifySmsCode(dto.getPhone(), dto.getCode());
        if (!valid) {
            throw new ServiceException("验证码错误");
        }

        // 查询或创建用户
        SysUser user = getOrCreateUserByPhone(dto.getPhone());

        // 生成 Token
        return buildAuthResponse(user);
    }

    @Override
    public AuthResponseVO refreshToken(String refreshToken) {
        log.info("刷新 Token");

        // TODO: 验证 refresh token
        // 这里暂时使用 mock 数据
        Long userId = mockVerifyRefreshToken(refreshToken);
        if (userId == null) {
            throw new ServiceException("Refresh Token 无效或已过期");
        }

        // 查询用户
        SysUser user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 重新登录生成新 Token
        StpUtil.logout(userId);
        StpUtil.login(userId);

        return buildAuthResponse(user);
    }

    @Override
    public void logout() {
        log.info("退出登录");
        StpUtil.logout();
    }

    /**
     * 根据 OpenID 查询或创建用户
     */
    private SysUser getOrCreateUserByOpenid(String openid, WxLoginDTO dto) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getWxMpOpenid, openid);
        SysUser user = getOne(queryWrapper);

        if (user == null) {
            // 创建新用户
            user = new SysUser();
            user.setWxMpOpenid(openid);
            user.setNickName(dto.getNickname() != null ? dto.getNickname() : "微信用户");
            user.setAvatar(dto.getAvatar());
            user.setSex(dto.getGender() != null ? String.valueOf(dto.getGender()) : "0");
            user.setUserType("0"); // 普通用户
            user.setStatus("0");   // 正常状态
            user.setDelFlag("0");  // 未删除
            save(user);
            log.info("创建新用户，userId: {}, openid: {}", user.getId(), openid);
        }

        return user;
    }

    /**
     * 根据手机号查询或创建用户
     */
    private SysUser getOrCreateUserByPhone(String phone) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getPhonenumber, phone);
        SysUser user = getOne(queryWrapper);

        if (user == null) {
            // 创建新用户
            user = new SysUser();
            user.setPhonenumber(phone);
            user.setUserName(phone); // 用户名使用手机号
            user.setNickName("手机用户");
            user.setUserType("0"); // 普通用户
            user.setStatus("0");   // 正常状态
            user.setDelFlag("0");  // 未删除
            save(user);
            log.info("创建新用户，userId: {}, phone: {}", user.getId(), phone);
        }

        return user;
    }

    /**
     * 构建认证响应
     */
    private AuthResponseVO buildAuthResponse(SysUser user) {
        // 登录并获取 Token
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 构建 Token VO
        TokenVO tokenVO = TokenVO.builder()
                .accessToken(tokenInfo.getTokenValue())
                .refreshToken(tokenInfo.getTokenValue()) // 简化处理，refresh token 与 access token 相同
                .expiresIn(TOKEN_TIMEOUT)
                .tokenType("Bearer")
                .build();

        // 构建用户信息 VO
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .avatar(user.getAvatar())
                .sex(user.getSex())
                .phonenumber(maskPhone(user.getPhonenumber()))
                .userType(user.getUserType())
                .build();

        // 更新最后登录时间
        user.setLoginDate(LocalDateTime.now());
        updateById(user);

        return AuthResponseVO.builder()
                .token(tokenVO)
                .userInfo(userInfoVO)
                .build();
    }

    /**
     * 手机号脱敏
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    // ==================== Mock 方法（实际开发时替换）=====================

    /**
     * Mock: 通过微信 code 获取 openid
     */
    private String mockGetWxOpenid(String code) {
        // TODO: 实际应该调用微信 API: https://api.weixin.qq.com/sns/jscode2session
        return "mock_openid_" + code;
    }

    /**
     * Mock: 验证短信验证码
     */
    private boolean mockVerifySmsCode(String phone, String code) {
        // TODO: 实际应该验证 Redis 中的验证码
        return "123456".equals(code);
    }

    /**
     * Mock: 验证 Refresh Token
     */
    private Long mockVerifyRefreshToken(String refreshToken) {
        // TODO: 实际应该验证 Redis 中的 refresh token
        return 1L;
    }
}
