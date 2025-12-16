package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.chargerecord.*;
import com.experiment.parkingsystem.service.ChargeRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChargeRecordController {

    private final ChargeRecordService recordService;

    public ChargeRecordController(ChargeRecordService recordService) {
        this.recordService = recordService;
    }

    // 5.1 生成收费记录
    @PostMapping("/charge-records")
    public ResponseEntity<ApiResponse<ChargeRecordCreateResponse>> createRecord(@RequestBody ChargeRecordCreateRequest request) {
        return new ResponseEntity<>(ApiResponse.success(recordService.createRecord(request)), HttpStatus.CREATED);
    }

    // 5.2 查询收费记录
    @GetMapping("/charge-records")
    public ResponseEntity<ApiResponse<PaginatedResponse<ChargeRecordListItemResponse>>> listRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String userId) {

        return ResponseEntity.ok(ApiResponse.success(
                recordService.listRecords(page, size, licensePlate, vehicleId, startTime, endTime, ownerId, userId)
        ));
    }

    // 5.3 计算停车费用
    @PostMapping("/charges/calculate")
    public ResponseEntity<ApiResponse<FeeCalculateResponse>> calculateFee(@RequestBody FeeCalculateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(recordService.calculateFee(request)));
    }
}