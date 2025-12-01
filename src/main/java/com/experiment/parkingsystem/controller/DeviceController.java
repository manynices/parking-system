package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
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

    /**
     * 6.1 新增设备
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponse>> createDevice(@RequestBody DeviceCreateRequest request) {
        DeviceResponse createdDevice = deviceService.createDevice(request);
        return new ResponseEntity<>(ApiResponse.success(createdDevice), HttpStatus.CREATED);
    }

    /**
     * 6.2 更新设备状态
     */
    @PutMapping("/{deviceId}/status")
    public ResponseEntity<ApiResponse<DeviceResponse>> updateDeviceStatus(
            @PathVariable String deviceId,
            @RequestBody DeviceStatusUpdateRequest request) {
        DeviceResponse updatedDevice = deviceService.updateDeviceStatus(deviceId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedDevice));
    }

    /**
     * 6.3 查询设备列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<DeviceResponse>>> getDevices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String parkingArea) {
        PaginatedResponse<DeviceResponse> devices = deviceService.getDevices(page, size, deviceType, status, parkingArea);
        return ResponseEntity.ok(ApiResponse.success(devices));
    }

    /**
     * 6.4 设备关联车位统计
     */
    @GetMapping("/{deviceId}/parking-stats")
    public ResponseEntity<ApiResponse<DeviceParkingStatsResponse>> getParkingStats(@PathVariable String deviceId) {
        DeviceParkingStatsResponse stats = deviceService.getParkingStats(deviceId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}