package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.user.*;
import com.experiment.parkingsystem.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserRegisterResponse>> register(@RequestBody UserRegisterRequest request) {
        return new ResponseEntity<>(ApiResponse.success(userService.register(request)), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponse>> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.login(request)));
    }

    @PostMapping("/forgot-password")
    // 【修改点】ApiResponse.success("msg") 返回的是 ApiResponse<Void>
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody UserForgotPasswordRequest request) {
        userService.forgotPassword(request);
        // 【修改点】你的 ApiResponse 里叫 success(String) 而不是 successMessage
        return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(userService.getCurrentUserProfile()));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(@RequestBody UserProfileUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUserProfile(request)));
    }

    @PutMapping("/change-password")
    // 【修改点】
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody UserChangePasswordRequest request) {
        userService.changePassword(request);
        // 【修改点】使用 success(String)
        return ResponseEntity.ok(ApiResponse.success("密码修改成功"));
    }

    @PostMapping("/owner-verification/apply")
    public ResponseEntity<ApiResponse<OwnerVerificationApplyResponse>> applyOwnerVerification(
            @RequestBody OwnerVerificationApplyRequest request) {
        return new ResponseEntity<>(ApiResponse.success(userService.applyOwnerVerification(request)), HttpStatus.CREATED);
    }

    @GetMapping("/owner-verification/status")
    public ResponseEntity<ApiResponse<OwnerVerificationStatusResponse>> getOwnerVerificationStatus() {
        return ResponseEntity.ok(ApiResponse.success(userService.getOwnerVerificationStatus()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<UserListItemResponse>>> listUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean isOwnerVerified,
            @RequestParam(required = false) String name) {

        return ResponseEntity.ok(ApiResponse.success(
                userService.listUsers(page, size, account, phone, isOwnerVerified, name)
        ));
    }
}