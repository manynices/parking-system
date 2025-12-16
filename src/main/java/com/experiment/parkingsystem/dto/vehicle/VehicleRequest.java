package com.experiment.parkingsystem.dto.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

@Data
public class VehicleRequest {
    private String licensePlate;
    private String vehicleType;

    // 前端传的是 String (如 "O001")，Service 层会解析为 Long
    private String ownerId;
    private String userId;

    private String status;
    private String vehicleClass;
    private String vehicleBrand;
    private String vehicleColor;
    private String engineNo;
    private String frameNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceExpiry;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate annualInspectionExpiry;
}