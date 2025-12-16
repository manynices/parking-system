package com.experiment.parkingsystem.dto.chargerecord;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ChargeRecordListItemResponse {
    private String recordId;
    private String vehicleId;
    private String licensePlate;
    private LocalDateTime enterTime;
    private LocalDateTime exitTime;
    private BigDecimal amount;
    private BigDecimal actualAmount;
    private BigDecimal discount;
    private String paymentMethod;
    private String adminName;
    private LocalDateTime createTime;
}