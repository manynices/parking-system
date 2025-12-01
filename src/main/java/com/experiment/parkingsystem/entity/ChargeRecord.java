package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeRecord {
    private String recordId;
    private String vehicleId;
    private Date enterTime;
    private Date exitTime;
    private BigDecimal amount; // 使用 BigDecimal 保证金额计算的精度
    private String paymentMethod;
    private String adminId;
}