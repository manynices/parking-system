package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ParkingSpaceItemResponse {
    private String spaceNo;
    private String parkingArea;
    private String spaceType;
    private String status;
    private String vehicleId;
    private String vehiclePlate;
    private String vehicleClass;
    private String ownerName;

    // 4.3 接口特有
    private BigDecimal hourlyRate;
    private BigDecimal dailyMax;

    // 4.4 接口特有
    private BigDecimal price;
    private String description;

    // 4.6 接口特有
    private String licensePlate; // 也可以复用 vehiclePlate
    private LocalDateTime purchaseTime;
    private BigDecimal purchaseAmount;
    private String paymentMethod;
}