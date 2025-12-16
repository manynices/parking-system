package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.monthlycard.*;
import com.experiment.parkingsystem.service.MonthlyCardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monthly-cards")
public class MonthlyCardController {

    private final MonthlyCardService cardService;

    public MonthlyCardController(MonthlyCardService cardService) {
        this.cardService = cardService;
    }

    // 3.1 办理月卡
    @PostMapping
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> createCard(@RequestBody MonthlyCardRequest request) {
        return new ResponseEntity<>(ApiResponse.success(cardService.createCard(request)), HttpStatus.CREATED);
    }

    // 3.2 月卡续费
    @PutMapping("/{cardId}/renew")
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> renewCard(
            @PathVariable String cardId,
            @RequestBody MonthlyCardRenewRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cardService.renewCard(cardId, request)));
    }

    // 3.3 月卡挂失/解挂
    @PutMapping("/{cardId}/status")
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> updateStatus(
            @PathVariable String cardId,
            @RequestBody MonthlyCardStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cardService.updateStatus(cardId, request)));
    }

    // 3.4 查询月卡列表
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<MonthlyCardResponse>>> listCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String userId) {
        return ResponseEntity.ok(ApiResponse.success(
                cardService.listCards(page, size, vehicleId, status, ownerId, userId)
        ));
    }

    // 3.5 查询单个月卡
    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<MonthlyCardResponse>> getCard(@PathVariable String cardId) {
        return ResponseEntity.ok(ApiResponse.success(cardService.getCardById(cardId)));
    }

    // 3.6 获取套餐信息
    @GetMapping("/packages")
    public ResponseEntity<ApiResponse<List<MonthlyCardPackageResponse>>> getPackages() {
        return ResponseEntity.ok(ApiResponse.success(cardService.getPackages()));
    }
}