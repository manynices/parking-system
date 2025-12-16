package com.experiment.parkingsystem.dto.chargerecord;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FeeCalculateResponse {
    private String licensePlate;
    private String vehicleClass;
    private LocalDateTime enterTime;
    private LocalDateTime exitTime;
    private Long parkingDuration; // 分钟
    private BigDecimal baseAmount;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private Boolean isOwner;
    private BigDecimal ownerDiscount;
    private Boolean hasPrivateSpace;
}