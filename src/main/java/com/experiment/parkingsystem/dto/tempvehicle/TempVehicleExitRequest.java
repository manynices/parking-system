package com.experiment.parkingsystem.dto.tempvehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TempVehicleExitRequest {
    private String licensePlate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    private String deviceId;
    private String imageUrl;
    private Long parkingDuration;
    private BigDecimal parkingFee;
    private String paymentMethod;
}