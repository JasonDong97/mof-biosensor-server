package com.miqroera.biosensor.domain.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.miqroera.biosensor.domain.mapper.SysUserMapper;
import com.miqroera.biosensor.domain.model.SysUser;
import com.miqroera.biosensor.domain.model.dto.PhoneLoginDTO;
import com.miqroera.biosensor.domain.model.dto.UserProfileUpdateDTO;
import com.miqroera.biosensor.domain.model.dto.WxLoginDTO;
import com.miqroera.biosensor.domain.model.vo.AuthResponseVO;
import com.miqroera.biosensor.domain.model.vo.TokenVO;
import com.miqroera.biosensor.domain.model.vo.UserInfoVO;
import com.miqroera.biosensor.domain.service.ISmsService;
import com.miqroera.biosensor.domain.service.ISysUserService;
import com.miqroera.biosensor.infra.config.WechatConfig;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.domain.model.LoginUser;
import com.miqroera.biosensor.infra.util.RedisUtil;
import com.miqroera.biosensor.infra.util.WeChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
    /**
     * Refresh Token 前缀
     */
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";
    /**
     * Refresh Token 过期时间（秒）- 30 天
     */
    private static final long REFRESH_TOKEN_TIMEOUT = 30 * 24 * 60 * 60;
    private static final String USER_WECHAT_LOGIN_SESSION_KEY = "auth:wx:session:";

    private final RedisUtil redisUtil;
    private final ISmsService smsService;
    private final WechatConfig wechatConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponseVO wxLogin(WxLoginDTO dto) {
        log.info("微信小程序登录，code: {}", dto.getCode());

        // 这里暂时使用 mock 数据，实际开发需要替换为真实的微信 API 调用
        var code = dto.getCode();
        String openid = WeChatUtil.code2Session(code, wechatConfig);
        Assert.notNull(openid, "微信登录失败，openid 为 null");
        // 查询或创建用户
        SysUser user = getOrCreateUserByOpenid(openid, dto);

        // 构建认证响应（带 Refresh Token）
        return buildAuthResponseWithRefreshToken(user);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public AuthResponseVO phoneLogin(PhoneLoginDTO dto) {
        log.info("手机号登录，phone: {}", dto.getPhone());

        boolean verified = smsService.verifyCode(dto.getPhone(), dto.getCode());
        Assert.isTrue(verified, "验证码错误");

        // 查询或创建用户
        SysUser user = getOrCreateUserByPhone(dto.getPhone());

        // 构建认证响应（带 Refresh Token）
        return buildAuthResponseWithRefreshToken(user);
    }

    @Override
    public AuthResponseVO refreshToken(String refreshToken) {
        log.info("刷新 Token");

        // 验证 Refresh Token
        Long userId = verifyRefreshToken(refreshToken);
        if (userId == null) {
            throw new ServiceException("Refresh Token 无效或已过期");
        }

        // 查询用户
        SysUser user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 检查用户状态
        if (!"0".equals(user.getStatus())) {
            throw new ServiceException("账号已被禁用");
        }

        // 重新登录生成新 Token
        StpUtil.logout(userId);
        StpUtil.login(userId);

        // 生成新的 Refresh Token
        return buildAuthResponseWithRefreshToken(user);
    }

    @Override
    public void logout() {
        log.info("退出登录");
        StpUtil.logout();
    }

    @Cacheable(value = "miqrobreath:user_profile", key = "#userId")
    @Override
    public UserInfoVO getUserProfile(Long userId) {
        log.info("获取用户信息，userId: {}", userId);
        SysUser user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        return UserInfoVO.builder()
                .id(user.getId())
                .openid(user.getWxMpOpenid())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .avatar(user.getAvatar())
                .sex(user.getSex())
                .phonenumber(maskPhone(user.getPhonenumber()))
                .userType(user.getUserType())
                .birthday(user.getBirthday())
                .height(user.getHeight())
                .weight(user.getWeight())
                .firstMeasureDate(user.getFirstMeasureDate())
                .totalMeasures(user.getTotalMeasures())
                .regTime(user.getRegTime())
                .build();
    }

    @Cacheable(value = "miqrobreath:login", key = "#userId")
    @Override
    public LoginUser getLoginUserById(Long userId) {
        log.info("获取登录用户信息，userId: {}", userId);
        SysUser user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setUserName(user.getUserName());
        loginUser.setNickName(user.getNickName());
        loginUser.setUserType(Integer.parseInt(user.getUserType()));
        loginUser.setIsAdmin("1".equals(user.getUserType()));
        loginUser.setPhonenumber(user.getPhonenumber());
        loginUser.setEmail(user.getEmail());
        loginUser.setSex(user.getSex());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setStatus(user.getStatus());
        loginUser.setRemark(user.getRemark());
        // LocalDateTime 转 Date
        if (user.getCreateTime() != null) {
            loginUser.setCreateTime(Timestamp.valueOf(user.getCreateTime()));
        }

        return loginUser;
    }

    @CacheEvict(value = "miqrobreath:user_profile", key = "#userId")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfile(Long userId, UserProfileUpdateDTO dto) {
        log.info("更新用户信息，userId: {}, dto: {}", userId, dto);

        SysUser user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (dto.getNickName() != null) {
            user.setNickName(dto.getNickName());
        }
        if (dto.getSex() != null) {
            user.setSex(dto.getSex());
        }
        if (dto.getAvatar() != null) {
            user.setAvatar(dto.getAvatar());
        }
        if (dto.getBirthday() != null) {
            user.setBirthday(dto.getBirthday());
        }
        if (dto.getHeight() != null) {
            user.setHeight(dto.getHeight());
        }
        if (dto.getWeight() != null) {
            user.setWeight(dto.getWeight());
        }

        updateById(user);
        log.info("用户信息更新成功，userId: {}", userId);
    }

    @CacheEvict(value = "miqrobreath:user_profile", allEntries = true)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser phoneBind(long userId, String phone) {
        this.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPhonenumber, phone)
                .update();
        return getById(userId);
    }

    /**
     * 根据 OpenID 查询或创建用户
     */
    private SysUser getOrCreateUserByOpenid(String openid, WxLoginDTO dto) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getWxMpOpenid, openid);
        SysUser user = getOne(queryWrapper);

        boolean isNew = user == null;

        if (isNew) {
            // 创建新用户
            user = new SysUser();
            user.setCreateTime(LocalDateTime.now());
            user.setRegTime(LocalDateTime.now());
        }

        user.setWxMpOpenid(openid);
        user.setNickName(dto.getNickname() != null ? dto.getNickname() : "微信用户");
        user.setAvatar(dto.getAvatar());
        user.setSex(dto.getGender() != null ? String.valueOf(dto.getGender()) : "0");
        user.setUserType("0"); // 普通用户
        user.setStatus("0");   // 正常状态
        user.setDelFlag("0");  // 未删除
        user.setPhonenumber(dto.getPhonenumber());
        if (isNew) {
            save(user);
            log.info("创建新用户，userId: {}, openid: {}", user.getId(), openid);
        } else {
            updateById(user);
            log.info("更新用户信息，userId: {}, openid: {}", user.getId(), openid);
        }
        return user;
    }

    /**
     * 根据手机号查询或创建用户
     */
    private SysUser getOrCreateUserByPhone(String phone) {
        SysUser user = this.lambdaQuery().eq(SysUser::getPhonenumber, phone).one();
        if (user == null) {
            // 创建新用户
            user = new SysUser();
            user.setPhonenumber(phone);
            user.setUserName(phone); // 用户名使用手机号
            user.setNickName("手机用户");
            user.setUserType("0"); // 普通用户
            user.setStatus("0");   // 正常状态
            user.setDelFlag("0");  // 未删除
            user.setCreateTime(LocalDateTime.now());
            user.setRegTime(LocalDateTime.now());
            save(user);
            log.info("创建新用户，userId: {}, phone: {}", user.getId(), phone);
        }

        return user;
    }

    /**
     * 构建认证响应（带 Refresh Token）
     */
    private AuthResponseVO buildAuthResponseWithRefreshToken(SysUser user) {
        // 登录并获取 Token
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 生成新的 Refresh Token
        String newRefreshToken = generateRefreshToken(user.getId());

        // 构建 Token VO
        TokenVO tokenVO = TokenVO.builder()
                .accessToken(tokenInfo.getTokenValue())
                .refreshToken(newRefreshToken)
                .expiresIn(TOKEN_TIMEOUT)
                .tokenType("Bearer")
                .build();

        // 构建用户信息 VO
        UserInfoVO userInfoVO = UserInfoVO.builder()
                .id(user.getId())
                .openid(user.getWxMpOpenid())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .avatar(user.getAvatar())
                .sex(user.getSex())
                .phonenumber(maskPhone(user.getPhonenumber()))
                .userType(user.getUserType())
                .birthday(user.getBirthday())
                .height(user.getHeight())
                .weight(user.getWeight())
                .firstMeasureDate(user.getFirstMeasureDate())
                .totalMeasures(user.getTotalMeasures())
                .regTime(user.getRegTime())
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
     * 生成 Refresh Token
     */
    private String generateRefreshToken(Long userId) {
        // 生成唯一的 refresh token（使用 UUID + userId）
        String refreshToken = "rt_" + userId + "_" + java.util.UUID.randomUUID().toString().replace("-", "");

        // 存储到 Redis，格式：auth:refresh:{token} -> userId
        String redisKey = REFRESH_TOKEN_PREFIX + refreshToken;
        redisUtil.set(redisKey, userId.toString(), REFRESH_TOKEN_TIMEOUT);

        log.debug("生成 Refresh Token: {}, userId: {}, 过期时间：{}秒", refreshToken, userId, REFRESH_TOKEN_TIMEOUT);

        return refreshToken;
    }

    /**
     * 验证 Refresh Token
     */
    private Long verifyRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return null;
        }

        // 从 Redis 中查询
        String redisKey = REFRESH_TOKEN_PREFIX + refreshToken;
        Object value = redisUtil.get(redisKey);

        if (value == null) {
            log.debug("Refresh Token 不存在或已过期：{}", refreshToken);
            return null;
        }

        try {
            Long userId = Long.parseLong(value.toString());
            log.debug("Refresh Token 验证成功：{}, userId: {}", refreshToken, userId);
            return userId;
        } catch (NumberFormatException e) {
            log.error("Refresh Token 格式错误：{}", refreshToken, e);
            return null;
        }
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
}
