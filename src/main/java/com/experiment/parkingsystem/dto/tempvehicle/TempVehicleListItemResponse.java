package com.experiment.parkingsystem.dto.tempvehicle;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TempVehicleListItemResponse {
    private String registrationId;
    private String licensePlate;
    private String applicantName;
    private String visitUnit;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private BigDecimal parkingFee;
    private String status;
    private LocalDateTime createTime;
}