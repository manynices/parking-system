package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.device.*;

public interface DeviceService {
    DeviceResponse addDevice(DeviceCreateRequest request);

    DeviceResponse updateDeviceStatus(String deviceIdStr, DeviceStatusUpdateRequest request);

    PaginatedResponse<DeviceResponse> listDevices(int page, int size, String deviceType, String status, String parkingArea);

    DeviceParkingStatsResponse getDeviceParkingStats(String deviceIdStr);
}