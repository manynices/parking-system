package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VehicleBindApplication {
    private Long applicationId;
    private Long userId;
    private String licensePlate;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleColor;
    private String proofImages; // JSON String
    private String remark;
    private String status;
    private LocalDateTime createTime;
}