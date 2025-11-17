package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.Vehicle;
import lombok.Data;

@Data
public class VehicleResponse {
    private String vehicleId;
    private String licensePlate;
    private String vehicleType;
    private String ownerId;
    private String status;

    public static VehicleResponse fromEntity(Vehicle vehicle) {
        VehicleResponse dto = new VehicleResponse();
        // 注意：这里需要将 Entity 的属性映射到 DTO
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setOwnerId(vehicle.getOwnerId());
        dto.setStatus(vehicle.getStatus());
        return dto;
    }
}