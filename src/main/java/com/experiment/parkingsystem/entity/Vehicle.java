package com.experiment.parkingsystem.entity;

import lombok.Data;

@Data
public class Vehicle {
    private String vehicleId; // 车辆的唯一标识
    private String licensePlate; // 车牌号
    private String vehicleType; // 车辆类型
    private String ownerId; // 所属业主 ID
    private String status; // 车辆状态 (在用/停用)
}