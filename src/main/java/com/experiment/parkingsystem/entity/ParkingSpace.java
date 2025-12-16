package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ParkingSpace {
    private String spaceNo;     // 主键
    private String parkingArea;
    private String spaceType;
    private String status;
    private Long vehicleId;
    private Long ownerId;
    private BigDecimal price;

    // 购买信息
    private LocalDateTime purchaseTime;
    private BigDecimal purchaseAmount;
    private String paymentMethod;
    private LocalDateTime updateTime;

    // --- 辅助字段 (用于关联查询) ---
    private String vehiclePlate;  // 关联 vehicle 表
    private String vehicleClass;  // 关联 vehicle 表
    private String ownerName;     // 关联 sys_user 表 (车位主)
}