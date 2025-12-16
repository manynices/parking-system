package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.vehicle.*;

import java.util.List;

public interface VehicleService {
    VehicleResponse addVehicle(VehicleRequest request);

    // 【修改点】这里必须是 VehicleBindResponse
    VehicleBindResponse applyBind(VehicleBindRequest request);

    PaginatedResponse<VehicleResponse> listVehicles(int page, int size, String ownerIdStr, String userIdStr, String status, String vehicleClass, String licensePlate);

    VehicleResponse getVehicleById(String vehicleIdStr);

    VehicleResponse updateVehicle(String vehicleIdStr, VehicleRequest request);

    void deleteVehicle(String vehicleIdStr);

    VehicleResponse getVehicleByPlate(String licensePlate);

    List<VehicleResponse> getMyVehicles();
}