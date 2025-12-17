package com.experiment.parkingsystem.dto.tempvehicle;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TempVehicleEntryResponse {
    private String registrationId;
    private String licensePlate;
    private LocalDateTime entryTime;
    private String status;
}