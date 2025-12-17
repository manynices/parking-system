package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.tempvehicle.*;
import com.experiment.parkingsystem.service.TempVehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp-vehicles")
public class TempVehicleController {

    private final TempVehicleService tempVehicleService;

    public TempVehicleController(TempVehicleService tempVehicleService) {
        this.tempVehicleService = tempVehicleService;
    }

    // 7.1 临时车辆登记
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<TempVehicleRegisterResponse>> register(@RequestBody TempVehicleRegisterRequest request) {
        return new ResponseEntity<>(ApiResponse.success(tempVehicleService.register(request)), HttpStatus.CREATED);
    }

    // 7.2 临时车辆入场记录
    @PostMapping("/entry")
    public ResponseEntity<ApiResponse<TempVehicleEntryResponse>> recordEntry(@RequestBody TempVehicleEntryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(tempVehicleService.recordEntry(request)));
    }

    // 7.3 临时车辆出场记录
    @PostMapping("/exit")
    public ResponseEntity<ApiResponse<TempVehicleExitResponse>> recordExit(@RequestBody TempVehicleExitRequest request) {
        return ResponseEntity.ok(ApiResponse.success(tempVehicleService.recordExit(request)));
    }

    // 7.4 查询临时车辆记录
    @GetMapping("/records")
    public ResponseEntity<ApiResponse<PaginatedResponse<TempVehicleListItemResponse>>> listRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        return ResponseEntity.ok(ApiResponse.success(
                tempVehicleService.listRecords(page, size, licensePlate, status, startDate, endDate)
        ));
    }
}