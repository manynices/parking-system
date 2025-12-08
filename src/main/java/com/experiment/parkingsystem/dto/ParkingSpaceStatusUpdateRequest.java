package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class ParkingSpaceStatusUpdateRequest {
    private String status; // "空闲" 或 "占用"
    private String vehicleId; // 占用时必填
    private String deviceId; // 关联的操作设备
}