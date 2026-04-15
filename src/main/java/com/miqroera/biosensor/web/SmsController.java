package com.miqroera.biosensor.web;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.miqroera.biosensor.domain.model.dto.SmsCodeVerifyDTO;
import com.miqroera.biosensor.domain.model.dto.SmsSendDTO;
import com.miqroera.biosensor.domain.service.ISmsService;
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
 * 短信验证码控制器
 *
 * @author dongjingxiang
 * @since 2026-04-14
 */
@Slf4j
@Tag(name = "短信", description = "短信验证码发送与验证")
@ApiSupport(order = 6)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/sms")
public class SmsController {

    private final ISmsService smsService;

    /**
     * 发送短信验证码
     */
    @PostMapping("/send")
    @Operation(summary = "发送短信验证码", description = "向指定手机号发送登录验证码")
    public R<Void> sendCode(@RequestBody @Valid SmsSendDTO dto) {
        return smsService.sendCode(dto.getPhoneNumber());
    }

    /**
     * 验证短信验证码
     */
    @PostMapping("/verify")
    @Operation(summary = "验证短信验证码", description = "验证用户输入的验证码是否正确")
    public R<Boolean> verifyCode(@RequestBody @Valid SmsCodeVerifyDTO dto) {
        boolean result = smsService.verifyCode(dto.getPhoneNumber(), dto.getCode());
        return R.ok(result);
    }
}
