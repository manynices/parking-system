package com.experiment.parkingsystem.dto.monthlycard;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MonthlyCardResponse {
    private String cardId;     // "MC001"
    private String cardNo;
    private String vehicleId;  // "V001"
    private String vehiclePlate;
    private String ownerName;
    private String packageType;
    private LocalDateTime issueTime;
    private LocalDateTime validityPeriod;
    private LocalDateTime renewTime;
    private String status;

    // 续费接口特有字段
    private BigDecimal amount;
    private LocalDateTime newValidityPeriod;
    private String paymentMethod;
}