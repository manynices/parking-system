package com.experiment.parkingsystem.dto.vehicle;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VehicleBindResponse {
    private String applicationId;
    private String licensePlate;
    private String vehicleType;
    private String status;
    private LocalDateTime createTime;
}