package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MonthlyCard {
    private Long cardId;
    private String cardNo;
    private Long vehicleId;
    private String packageType;
    private BigDecimal amount;
    private String paymentMethod;
    private String invoiceNo;
    private LocalDateTime issueTime;
    private LocalDateTime validityPeriod;
    private LocalDateTime renewTime;
    private String status;
    private LocalDateTime createTime;

    // --- 辅助字段 (用于列表查询展示) ---
    private String vehiclePlate; // 车牌号
    private String ownerName;    // 车主姓名
}