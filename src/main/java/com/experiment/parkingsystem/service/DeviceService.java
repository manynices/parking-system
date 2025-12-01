package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;

public interface DeviceService {
    DeviceResponse createDevice(DeviceCreateRequest request);
    DeviceResponse updateDeviceStatus(String deviceId, DeviceStatusUpdateRequest request);
    PaginatedResponse<DeviceResponse> getDevices(int page, int size, String deviceType, String status, String parkingArea);
    DeviceParkingStatsResponse getParkingStats(String deviceId);
}