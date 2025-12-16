package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.dto.parkingspace.*;
import com.experiment.parkingsystem.service.ParkingSpaceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService spaceService;

    public ParkingSpaceController(ParkingSpaceService spaceService) {
        this.spaceService = spaceService;
    }

    // 4.1 查询车位状态
    @GetMapping
    public ResponseEntity<ApiResponse<ParkingSpaceStatsResponse>> getParkingStats(
            @RequestParam String parkingArea,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String spaceNo,
            @RequestParam(required = false) String spaceType) {
        return ResponseEntity.ok(ApiResponse.success(
                spaceService.getParkingStats(parkingArea, status, spaceNo, spaceType)
        ));
    }

    // 4.2 更新车位状态
    @PutMapping("/{spaceNo}/status")
    public ResponseEntity<ApiResponse<ParkingSpaceItemResponse>> updateStatus(
            @PathVariable String spaceNo,
            @RequestBody ParkingSpaceStatusUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                spaceService.updateSpaceStatus(spaceNo, request)
        ));
    }

    // 4.3 用户查询可用车位
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<ParkingSpaceAvailableResponse>> getAvailableSpaces(
            @RequestParam(required = false) String parkingArea,
            @RequestParam(defaultValue = "公共车位") String spaceType) {
        return ResponseEntity.ok(ApiResponse.success(
                spaceService.getAvailableSpaces(parkingArea, spaceType)
        ));
    }

    // 4.4 查询可购买的私人车位
    @GetMapping("/private/for-sale")
    public ResponseEntity<ApiResponse<ParkingSpaceSalesResponse>> getPrivateSpacesForSale(
            @RequestParam(required = false) String parkingArea) {
        return ResponseEntity.ok(ApiResponse.success(
                spaceService.getPrivateSpacesForSale(parkingArea)
        ));
    }

    // 4.5 业主购买私人车位
    @PostMapping("/private/purchase")
    public ResponseEntity<ApiResponse<ParkingSpacePurchaseResponse>> purchaseSpace(
            @RequestBody ParkingSpacePurchaseRequest request) {
        return new ResponseEntity<>(ApiResponse.success(
                spaceService.purchaseSpace(request)
        ), HttpStatus.CREATED);
    }

    // 4.6 用户查询已购买的车位
    @GetMapping("/my-private-spaces")
    public ResponseEntity<ApiResponse<List<ParkingSpaceItemResponse>>> getMyPrivateSpaces() {
        return ResponseEntity.ok(ApiResponse.success(
                spaceService.getMyPrivateSpaces()
        ));
    }
}