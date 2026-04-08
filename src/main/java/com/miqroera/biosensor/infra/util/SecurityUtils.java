
package com.miqroera.biosensor.infra.util;

import cn.dev33.satoken.stp.StpUtil;
import com.miqroera.biosensor.domain.service.ISysUserService;
import com.miqroera.biosensor.infra.domain.model.LoginUser;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author Lion Li
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SecurityUtils {

    @Autowired
    private ISysUserService userService;

    @PostConstruct
    public void init() {
        SecurityUtils.getLoginUser = userId -> userService.getLoginUserById(Long.valueOf(userId.toString()));
    }

    public static final Long SUPER_ADMIN_ID = 1L;
    public static Function<Object, LoginUser> getLoginUser;

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        StpUtil.login(loginUser.getId());
    }

    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        Object userId = StpUtil.getLoginId();
        synchronized (userId) {
            if (getLoginUser == null) {
                throw new UnsupportedOperationException("获取登录用户函数未注入!");
            }
            return getLoginUser.apply(userId);
        }
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        return getLoginUser().getId();
    }

    /**
     * 获取用户昵称
     */
    public static String getNickName() {
        try {
            return getLoginUser().getNickName();
        } catch (Exception e) {
            return "未登录用户";
        }
    }

    public static boolean isAdmin() {
        return getLoginUser().getIsAdmin() != null && getLoginUser().getIsAdmin();
    }

    public static boolean isNotAdmin() {
        return !isAdmin();
    }

    public static boolean isLoginUser(@NotNull Long userId) {
        return getUserId().equals(userId);
    }

    public static String getUsername() {
        return getLoginUser().getUserName();
    }

    public static boolean isSuperAdmin(Long userId) {
        return SUPER_ADMIN_ID.equals(userId);
    }

    public static boolean isSuperAdmin() {
        return isSuperAdmin(getUserId());
    }
}
