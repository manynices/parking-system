package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class VehicleCreateRequest {
    private String licensePlate;
    private String vehicleType;
    private String ownerId;
    private String status;
}