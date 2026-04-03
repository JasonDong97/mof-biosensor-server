package com.miqroera.biosensor.web;

import cn.dev33.satoken.annotation.SaIgnore;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.miqroera.biosensor.domain.model.dto.PhoneLoginDTO;
import com.miqroera.biosensor.domain.model.dto.RefreshTokenDTO;
import com.miqroera.biosensor.domain.model.dto.WxLoginDTO;
import com.miqroera.biosensor.domain.model.vo.AuthResponseVO;
import com.miqroera.biosensor.domain.service.ISysUserService;
import com.miqroera.biosensor.infra.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * @author dongjingxiang
 * @since 2026-03-26
 */
@Slf4j
@Tag(name = "认证管理", description = "登录、登出、Token 刷新")
@ApiSort(value = 2)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final ISysUserService sysUserService;

    /**
     * 微信小程序登录
     */
    @SaIgnore
    @PostMapping("/login/wx")
    @Operation(summary = "微信小程序登录", description = "通过微信 code 进行登录")
    public R<AuthResponseVO> wxLogin(@Valid @RequestBody WxLoginDTO dto) {
        AuthResponseVO response = sysUserService.wxLogin(dto);
        return R.ok(response);
    }

    /**
     * 手机号登录
     */
    @SaIgnore
    @PostMapping("/login/phone")
    @Operation(summary = "手机号登录", description = "通过手机号 + 短信验证码进行登录")
    public R<AuthResponseVO> phoneLogin(@Valid @RequestBody PhoneLoginDTO dto) {
        AuthResponseVO response = sysUserService.phoneLogin(dto);
        return R.ok(response);
    }

    /**
     * 刷新 Token
     */
    @SaIgnore
    @PostMapping("/refresh")
    @Operation(summary = "刷新 Token", description = "通过 refresh token 获取新的 access token")
    public R<AuthResponseVO> refreshToken(@RequestBody RefreshTokenDTO refreshToken) {
        AuthResponseVO response = sysUserService.refreshToken(refreshToken.getRefreshToken());
        return R.ok(response);
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "注销当前用户的登录状态")
    public R<Void> logout() {
        sysUserService.logout();
        return R.ok();
    }
}
