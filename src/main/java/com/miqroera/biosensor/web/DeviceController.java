package com.miqroera.biosensor.web;

import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.miqroera.biosensor.domain.model.dto.DeviceBindDTO;
import com.miqroera.biosensor.domain.model.dto.DeviceUnbindDTO;
import com.miqroera.biosensor.domain.model.vo.DeviceListVO;
import com.miqroera.biosensor.domain.model.vo.DeviceVO;
import com.miqroera.biosensor.domain.service.IUserDeviceService;
import com.miqroera.biosensor.infra.domain.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备控制器
 *
 * @author dongjingxiang
 * @since 2026-03-31
 */
@Slf4j
@Tag(name = "设备管理", description = "设备绑定、解绑、查询")
@ApiSort(value = 3)
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/devices")
public class DeviceController {

    private final IUserDeviceService userDeviceService;

    /**
     * 绑定设备
     */
    @PostMapping("/bind")
    @Operation(summary = "绑定设备", description = "绑定设备到当前用户")
    public R<DeviceVO> bindDevice(@Valid @RequestBody DeviceBindDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        DeviceVO deviceVO = userDeviceService.bindDevice(userId, dto);
        return R.ok(deviceVO);
    }

    /**
     * 解绑设备
     */
    @PostMapping("/unbind")
    @Operation(summary = "解绑设备", description = "解绑当前用户的设备")
    public R<Void> unbindDevice(@Valid @RequestBody DeviceUnbindDTO dto) {
        Long userId = StpUtil.getLoginIdAsLong();
        userDeviceService.unbindDevice(userId, dto);
        return R.ok();
    }

    /**
     * 获取设备列表
     */
    @GetMapping
    @Operation(summary = "获取设备列表", description = "获取当前用户已绑定的设备列表")
    public R<List<DeviceListVO>> getDeviceList() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<DeviceListVO> deviceList = userDeviceService.getUserDeviceList(userId);
        return R.ok(deviceList);
    }

    /**
     * 获取设备详情
     */
    @GetMapping("/{deviceSn}")
    @Operation(summary = "获取设备详情", description = "获取指定设备的详细信息")
    public R<DeviceVO> getDeviceDetail(@PathVariable String deviceSn) {
        Long userId = StpUtil.getLoginIdAsLong();
        DeviceVO deviceVO = userDeviceService.getDeviceDetail(userId, deviceSn);
        return R.ok(deviceVO);
    }
}