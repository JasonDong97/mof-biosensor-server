package com.miqroera.biosensor.web.admin;

import com.miqroera.biosensor.domain.model.dto.DeviceAddDTO;
import com.miqroera.biosensor.domain.model.dto.DeviceUpdateDTO;
import com.miqroera.biosensor.domain.model.vo.DeviceVO;
import com.miqroera.biosensor.domain.service.IUserDeviceService;
import com.miqroera.biosensor.infra.domain.exception.ServiceException;
import com.miqroera.biosensor.infra.domain.model.R;
import com.miqroera.biosensor.infra.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端设备控制器
 *
 * @author dongjingxiang
 * @since 2026-04-08
 */
@Slf4j
@Tag(name = "管理端设备", description = "管理端设备管理接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/admin/devices")
public class AdminDeviceController {

    private final IUserDeviceService userDeviceService;

    /**
     * 添加设备
     */
    @PostMapping
    @Operation(summary = "添加设备", description = "管理员添加新设备到设备库")
    public R<DeviceVO> addDevice(@Valid @RequestBody DeviceAddDTO dto) {
        // 权限校验：只允许 userId=1 或 userType=1 的用户操作
        if (!SecurityUtils.isSuperAdmin() && SecurityUtils.getLoginUser().getUserType() != 1) {
            throw new ServiceException("无权操作");
        }

        DeviceVO deviceVO = userDeviceService.addDevice(dto);
        return R.ok(deviceVO);
    }

    /**
     * 修改设备信息
     */
    @PutMapping("/{deviceId}")
    @Operation(summary = "修改设备信息", description = "管理员修改设备信息（MAC、版本、状态等）")
    public R<DeviceVO> updateDevice(@PathVariable Long deviceId, @Valid @RequestBody DeviceUpdateDTO dto) {
        // 权限校验：只允许 userId=1 或 userType=1 的用户操作
        if (!SecurityUtils.isSuperAdmin() && SecurityUtils.getLoginUser().getUserType() != 1) {
            throw new ServiceException("无权操作");
        }

        DeviceVO deviceVO = userDeviceService.updateDevice(deviceId, dto);
        return R.ok(deviceVO);
    }
}
