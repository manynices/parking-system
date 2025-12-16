package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.vehicle.*;
import com.experiment.parkingsystem.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // 2.1 管理员新增车辆
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> addVehicle(@RequestBody VehicleRequest request) {
        return new ResponseEntity<>(ApiResponse.success(vehicleService.addVehicle(request)), HttpStatus.CREATED);
    }

    // 2.2 用户申请绑定车辆
    @PostMapping("/bind-application")
    public ResponseEntity<ApiResponse<VehicleBindResponse>> applyBind(@RequestBody VehicleBindRequest request) {
        return new ResponseEntity<>(ApiResponse.success(vehicleService.applyBind(request)), HttpStatus.CREATED);
    }

    // 2.3 查询车辆列表
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<VehicleResponse>>> listVehicles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String vehicleClass,
            @RequestParam(required = false) String licensePlate) {

        return ResponseEntity.ok(ApiResponse.success(
                vehicleService.listVehicles(page, size, ownerId, userId, status, vehicleClass, licensePlate)
        ));
    }

    // 2.4 查询单个车辆
    @GetMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicle(@PathVariable String vehicleId) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getVehicleById(vehicleId)));
    }

    // 2.5 更新车辆信息
    @PutMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable String vehicleId,
            @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.updateVehicle(vehicleId, request)));
    }

    // 2.6 删除车辆
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable String vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        // 【修改点】之前是 successMessage，改为 success 以匹配你的 ApiResponse 定义
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }

    // 2.7 根据车牌号查询车辆
    @GetMapping("/by-plate/{licensePlate}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleByPlate(@PathVariable String licensePlate) {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getVehicleByPlate(licensePlate)));
    }

    // 2.8 用户查询自己的车辆
    @GetMapping("/my-vehicles")
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getMyVehicles() {
        return ResponseEntity.ok(ApiResponse.success(vehicleService.getMyVehicles()));
    }
}