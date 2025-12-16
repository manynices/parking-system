package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;

@Data
public class ParkingSpacePurchaseRequest {
    private String spaceNo;
    private String vehicleId;
    private String paymentMethod;
}