package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.ChargeRecordCreateRequest;
import com.experiment.parkingsystem.dto.ChargeRecordResponse;
import com.experiment.parkingsystem.service.ChargeRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/charge-records")
public class ChargeRecordController {

    private final ChargeRecordService chargeRecordService;

    public ChargeRecordController(ChargeRecordService chargeRecordService) {
        this.chargeRecordService = chargeRecordService;
    }

    /**
     * 5.1 生成收费记录
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ChargeRecordResponse>> createChargeRecord(@RequestBody ChargeRecordCreateRequest request) {
        ChargeRecordResponse createdRecord = chargeRecordService.createChargeRecord(request);
        return new ResponseEntity<>(ApiResponse.success(createdRecord), HttpStatus.CREATED);
    }

    /**
     * 5.2 查询收费记录
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<ChargeRecordResponse>>> getChargeRecords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String licensePlate,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String adminId,
            // @DateTimeFormat 用于将字符串格式的日期参数转换为 Date 对象
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime
    ) {
        PaginatedResponse<ChargeRecordResponse> records = chargeRecordService.getChargeRecords(page, size, licensePlate, vehicleId, adminId, startTime, endTime);
        return ResponseEntity.ok(ApiResponse.success(records));
    }
}