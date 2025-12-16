package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;

@Data
public class ParkingSpaceStatusUpdateRequest {
    private String status;
    private String vehicleId;
    private String deviceId;
}