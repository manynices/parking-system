package com.experiment.parkingsystem.dto.monthlycard;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MonthlyCardRequest {
    private String cardNo;
    private String vehicleId; // 前端传 "V001"
    private String packageType;
    private LocalDate startDate;
    private String paymentMethod;
    private BigDecimal amount;
    private String invoiceNo;
}