package com.experiment.parkingsystem.dto.vehicle;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VehicleResponse {
    // 【修改点】 必须是 String，用来接收 "V001" 这样的字符串
    private String vehicleId;

    private String licensePlate;
    private String vehicleType;

    // 【修改点】 必须是 String，用来接收 "O001"
    private String ownerId;
    private String ownerName;

    // 【修改点】 必须是 String，用来接收 "U001"
    private String userId;
    private String userName;

    private String status;
    private String vehicleClass;
    private String vehicleBrand;
    private String vehicleColor;
    private String engineNo;
    private String frameNo;

    private LocalDate insuranceExpiry;
    private LocalDate annualInspectionExpiry;
    private LocalDateTime createTime;

    private Boolean isPrivateSpace;
    private String privateSpaceNo;
    private Boolean hasMonthlyCard;
    private LocalDate monthlyCardExpiry;
}