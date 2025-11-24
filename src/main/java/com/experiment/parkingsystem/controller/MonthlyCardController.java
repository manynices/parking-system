package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.service.MonthlyCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monthly-cards") // API 请求路径前缀
public class MonthlyCardController {

    private final MonthlyCardService monthlyCardService;

    public MonthlyCardController(MonthlyCardService monthlyCardService) {
        this.monthlyCardService = monthlyCardService;
    }

    /**
     * 3.1 办理月卡
     * @param request 办理月卡的请求体
     * @return 新创建的月卡信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> createMonthlyCard(@RequestBody MonthlyCardCreateRequest request) {
        MonthlyCardResponse createdCard = monthlyCardService.createMonthlyCard(request);
        // 返回 201 Created 状态码
        return new ResponseEntity<>(ApiResponse.success(createdCard), HttpStatus.CREATED);
    }

    /**
     * 3.2 月卡续费
     * @param cardNo 要续费的月卡卡号
     * @param request 续费请求体
     * @return 更新后的月卡信息
     */
    @PutMapping("/{cardNo}/renew")
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> renewMonthlyCard(
            @PathVariable String cardNo,
            @RequestBody MonthlyCardRenewRequest request) {
        MonthlyCardResponse updatedCard = monthlyCardService.renewMonthlyCard(cardNo, request);
        // 返回 200 OK 状态码
        return ResponseEntity.ok(ApiResponse.success(updatedCard));
    }

    /**
     * 3.3 月卡挂失 / 解挂
     * @param cardNo 要更新状态的月卡号
     * @param request 状态更新请求体
     * @return 更新状态后的月卡信息
     */
    @PutMapping("/{cardNo}/status")
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> updateMonthlyCardStatus(
            @PathVariable String cardNo,
            @RequestBody MonthlyCardStatusRequest request) {
        MonthlyCardResponse updatedCard = monthlyCardService.updateMonthlyCardStatus(cardNo, request);
        // 返回 200 OK 状态码
        return ResponseEntity.ok(ApiResponse.success(updatedCard));
    }

    /**
     * 3.4 查询月卡列表
     * @param page 页码
     * @param size 每页数量
     * @param vehicleId 车辆ID (可选过滤条件)
     * @param status 月卡状态 (可选过滤条件)
     * @return 分页的月卡列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<MonthlyCardResponse>>> getMonthlyCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String status) {

        PaginatedResponse<MonthlyCardResponse> cards = monthlyCardService.getMonthlyCards(page, size, vehicleId, status);
        // 返回 200 OK 状态码
        return ResponseEntity.ok(ApiResponse.success(cards));
    }
}