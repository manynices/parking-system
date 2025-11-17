package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class VehicleUpdateRequest {
    // 这些字段都是可选的，表示用户可能只想更新其中一部分信息
    private String licensePlate;
    private String vehicleType;
    private String ownerId;
    private String status;
}