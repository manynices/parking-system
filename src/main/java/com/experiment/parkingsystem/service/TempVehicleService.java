package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.tempvehicle.*;

public interface TempVehicleService {
    TempVehicleRegisterResponse register(TempVehicleRegisterRequest request);

    TempVehicleEntryResponse recordEntry(TempVehicleEntryRequest request);

    TempVehicleExitResponse recordExit(TempVehicleExitRequest request);

    PaginatedResponse<TempVehicleListItemResponse> listRecords(int page, int size, String licensePlate, String status, String startDate, String endDate);
}