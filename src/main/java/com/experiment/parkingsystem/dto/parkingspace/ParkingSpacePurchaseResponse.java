package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ParkingSpacePurchaseResponse {
    private String spaceNo;
    private String vehicleId;
    private String licensePlate;
    private String ownerName;
    private BigDecimal amount;
    private String paymentMethod;
    private LocalDateTime purchaseTime;
    private String status;
}