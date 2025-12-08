package com.experiment.parkingsystem.controller;
import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.dto.ParkingAreaStatusResponse;
import com.experiment.parkingsystem.dto.ParkingSpaceStatusUpdateRequest;
import com.experiment.parkingsystem.dto.ParkingSpaceUpdateResponse;
import com.experiment.parkingsystem.service.ParkingSpaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/parking-spaces")
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;
    public ParkingSpaceController(ParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }
    @GetMapping
    public ResponseEntity<ApiResponse<ParkingAreaStatusResponse>> getParkingAreaStatus(
            @RequestParam String parkingArea,
            @RequestParam(required = false) String status) {
        ParkingAreaStatusResponse response = parkingSpaceService.getParkingAreaStatus(parkingArea, status);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @PutMapping("/{spaceNo}/status")
    public ResponseEntity<ApiResponse<ParkingSpaceUpdateResponse>> updateParkingSpaceStatus(
            @PathVariable String spaceNo,
            @RequestBody ParkingSpaceStatusUpdateRequest request) {
        ParkingSpaceUpdateResponse updatedSpace = parkingSpaceService.updateParkingSpaceStatus(spaceNo, request);
        return ResponseEntity.ok(ApiResponse.success(updatedSpace));
    }
}