package com.experiment.parkingsystem.dto.tempvehicle;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TempVehicleExitResponse {
    private String registrationId;
    private String licensePlate;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long parkingDuration;
    private BigDecimal parkingFee;
    private String paymentMethod;
    private String status;
}