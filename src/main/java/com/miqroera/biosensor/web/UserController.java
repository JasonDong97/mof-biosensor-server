package com.miqroera.biosensor.web;

import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.miqroera.biosensor.domain.model.dto.UserProfileUpdateDTO;
import com.miqroera.biosensor.domain.model.vo.UserInfoVO;
import com.miqroera.biosensor.domain.service.ISysUserService;
import com.miqroera.biosensor.infra.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author dongjingxiang
 * @since 2026-03-27
 */
@Slf4j
@Tag(name = "用户", description = "用户信息获取与更新")
@ApiSupport(order = 2)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final ISysUserService sysUserService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/profile")
    @Operation(summary = "获取用户信息", description = "获取当前登录用户的详细信息")
    public R<UserInfoVO> getUserProfile() {
        Long userId = StpUtil.getLoginIdAsLong();
        UserInfoVO userInfo = sysUserService.getUserProfile(userId);
        return R.ok(userInfo);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的详细信息")
    public R<Void> updateUserProfile(@Valid @RequestBody UserProfileUpdateDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        sysUserService.updateUserProfile(userId, dto);
        return R.ok();
    }
}