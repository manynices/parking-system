package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.administrator.*;
import com.experiment.parkingsystem.service.AdministratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    private final AdministratorService adminService;

    public AdministratorController(AdministratorService adminService) {
        this.adminService = adminService;
    }

    // 8.1 新增管理员
    @PostMapping
    public ResponseEntity<ApiResponse<AdminResponse>> createAdmin(@RequestBody AdminCreateRequest request) {
        return new ResponseEntity<>(ApiResponse.success(adminService.createAdmin(request)), HttpStatus.CREATED);
    }

    // 8.2 管理员登录
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AdminLoginResponse>> login(@RequestBody AdminLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(adminService.login(request)));
    }

    // 8.3 查询管理员列表
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<AdminResponse>>> listAdmins(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name) {

        return ResponseEntity.ok(ApiResponse.success(
                adminService.listAdmins(page, size, name)
        ));
    }

    // 8.4 查询单个管理员
    @GetMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminResponse>> getAdmin(@PathVariable String adminId) {
        return ResponseEntity.ok(ApiResponse.success(adminService.getAdminById(adminId)));
    }

    // 8.5 更新管理员信息
    @PutMapping("/{adminId}")
    public ResponseEntity<ApiResponse<AdminResponse>> updateAdmin(
            @PathVariable String adminId,
            @RequestBody AdminUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(adminService.updateAdmin(adminId, request)));
    }

    // 8.6 删除管理员
    @DeleteMapping("/{adminId}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(@PathVariable String adminId) {
        adminService.deleteAdmin(adminId);
        // 使用 success(String) 返回 ApiResponse<Void>
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }
}