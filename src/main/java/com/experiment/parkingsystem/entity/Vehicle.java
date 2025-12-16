package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Vehicle {
    // --- 数据库存在的字段 ---
    private Long vehicleId;
    private String licensePlate;
    private String vehicleType;
    private Long ownerId;
    private Long userId;
    private String status;
    private String vehicleClass;
    private String vehicleBrand;
    private String vehicleColor;
    private String engineNo;
    private String frameNo;
    private LocalDate insuranceExpiry;
    private LocalDate annualInspectionExpiry;
    private LocalDateTime createTime;

    // --- 【关键修复】辅助字段 (数据库表中没有，但 XML 查询结果里有) ---
    // 如果缺少这俩，getCodeName() 和 getOwnerName() 就会报错
    private String ownerName;
    private String userName;
}