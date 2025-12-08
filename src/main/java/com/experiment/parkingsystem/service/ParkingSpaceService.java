package com.experiment.parkingsystem.service;
import com.experiment.parkingsystem.dto.ParkingAreaStatusResponse;
import com.experiment.parkingsystem.dto.ParkingSpaceStatusUpdateRequest;
import com.experiment.parkingsystem.dto.ParkingSpaceUpdateResponse;
public interface ParkingSpaceService {
    ParkingAreaStatusResponse getParkingAreaStatus(String parkingArea, String status);
    ParkingSpaceUpdateResponse updateParkingSpaceStatus(String spaceNo, ParkingSpaceStatusUpdateRequest request);
}