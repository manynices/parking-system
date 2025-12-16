package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.VehicleCreateRequest;
import com.experiment.parkingsystem.dto.VehicleResponse;
import com.experiment.parkingsystem.dto.VehicleUpdateRequest;

public interface VehicleService {
    VehicleResponse createVehicle(VehicleCreateRequest request);
    com.experiment.parkingsystem.common.PaginatedResponse<VehicleResponse> getVehicles(int page, int size, String ownerId, String status, String licensePlate);
    VehicleResponse getVehicleById(String vehicleId);
    VehicleResponse updateVehicle(String vehicleId, VehicleUpdateRequest request);
    void deleteVehicle(String vehicleId);
}