package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.device.*;
import com.experiment.parkingsystem.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    // 6.1 新增设备
    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> addDevice(@RequestBody DeviceCreateRequest request) {
        return new ResponseEntity<>(ApiResponse.success(deviceService.addDevice(request)), HttpStatus.CREATED);
    }

    // 6.2 更新设备状态
    @PutMapping("/{deviceId}/status")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateStatus(
            @PathVariable String deviceId,
            @RequestBody DeviceStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(deviceService.updateDeviceStatus(deviceId, request)));
    }

    // 6.3 查询设备列表
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<DeviceResponse>>> listDevices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String parkingArea) {

        return ResponseEntity.ok(ApiResponse.success(
                deviceService.listDevices(page, size, deviceType, status, parkingArea)
        ));
    }

    // 6.4 设备关联车位统计
    @GetMapping("/{deviceId}/parking-stats")
    public ResponseEntity<ApiResponse<DeviceParkingStatsResponse>> getParkingStats(@PathVariable String deviceId) {
        return ResponseEntity.ok(ApiResponse.success(deviceService.getDeviceParkingStats(deviceId)));
    }
}