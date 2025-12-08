package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.ParkingSpace;
import lombok.Data;

@Data
public class ParkingSpaceResponse {
    private String spaceNo;
    private String status;
    private String vehicleId;

    public static ParkingSpaceResponse fromEntity(ParkingSpace space) {
        ParkingSpaceResponse dto = new ParkingSpaceResponse();
        dto.setSpaceNo(space.getSpaceNo());
        dto.setStatus(space.getStatus());
        dto.setVehicleId(space.getVehicleId());
        return dto;
    }
}