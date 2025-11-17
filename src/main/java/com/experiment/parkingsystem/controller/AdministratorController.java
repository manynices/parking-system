package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.service.AdministratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrators") // API 请求路径前缀
public class AdministratorController {

    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    /**
     * 新增管理员
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdministratorResponse>> createAdministrator(@RequestBody AdministratorCreateRequest request) {
        AdministratorResponse createdAdmin = administratorService.createAdministrator(request);
        return new ResponseEntity<>(ApiResponse.success(createdAdmin), HttpStatus.CREATED);
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdministratorLoginResponse>> login(@RequestBody AdministratorLoginRequest request) {
        AdministratorLoginResponse loginResponse = administratorService.login(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }

    /**
     * 查询管理员列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<AdministratorResponse>>> getAdministrators(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        PaginatedResponse<AdministratorResponse> admins = administratorService.getAdministrators(page, size);
        return ResponseEntity.ok(ApiResponse.success(admins));
    }

    /**
     * 根据ID查询单个管理员
     */
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdministratorResponse>> getAdministratorById(@PathVariable String adminId) {
        AdministratorResponse admin = administratorService.getAdministratorById(adminId);
        return ResponseEntity.ok(ApiResponse.success(admin));
    }

    /**
     * 更新管理员信息
     */
    @PutMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdministratorResponse>> updateAdministrator(
            @PathVariable String adminId,
            @RequestBody AdministratorUpdateRequest request) {
        AdministratorResponse updatedAdmin = administratorService.updateAdministrator(adminId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedAdmin));
    }

    /**
     * 删除管理员
     */
    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse<Void>> deleteAdministrator(@PathVariable String adminId) {
        administratorService.deleteAdministrator(adminId);
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }
}