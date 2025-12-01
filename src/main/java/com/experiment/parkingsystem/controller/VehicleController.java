package com.experiment.parkingsystem.controller;

import com.experiment.parkingsystem.common.ApiResponse;
import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.VehicleCreateRequest;
import com.experiment.parkingsystem.dto.VehicleResponse;
import com.experiment.parkingsystem.dto.VehicleUpdateRequest;
import com.experiment.parkingsystem.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles") // API 请求路径前缀
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * 新增车辆
     * @param request 车辆创建请求体
     * @return 响应新创建的车辆信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> createVehicle(@RequestBody VehicleCreateRequest request) {
        VehicleResponse createdVehicle = vehicleService.createVehicle(request);
        // 返回 201 Created 状态码
        return new ResponseEntity<>(ApiResponse.success(createdVehicle), HttpStatus.CREATED);
    }

    /**
     * 查询车辆列表
     * @param page 页码 (默认 1)
     * @param size 每页数量 (默认 10)
     * @param ownerId 所属业主ID (可选)
     * @param status 车辆状态 (可选)
     * @return 分页的车辆列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<VehicleResponse>>> getVehicles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String status) {

        PaginatedResponse<VehicleResponse> vehicles = vehicleService.getVehicles(page, size, ownerId, status);
        // 返回 200 OK 状态码
        return ResponseEntity.ok(ApiResponse.success(vehicles));
    }

    /**
     * 根据车牌号查询单个车辆
     * @param licensePlate 车辆车牌号
     * @return 单个车辆信息
     */
    @GetMapping("/{licensePlate}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@PathVariable String licensePlate) {
        VehicleResponse vehicle = vehicleService.getVehicleById(licensePlate);
        // 返回 200 OK 状态码
        return ResponseEntity.ok(ApiResponse.success(vehicle));
    }

//    @GetMapping("/detail")
//    public ResponseEntity<ApiResponse<VehicleResponse>> getVehicleById(@RequestParam String licensePlate) {
//        VehicleResponse vehicle = vehicleService.getVehicleById(licensePlate);
//        // 返回 200 OK 状态码
//        return ResponseEntity.ok(ApiResponse.success(vehicle));
//    }
    /**
     * 更新车辆信息
     * @param licensePlate 要更新的车车牌号
     * @param request 车辆更新请求体 (字段可选)
     * @return 更新后的车辆信息
     */
    @PutMapping("/{licensePlate}")
    public ResponseEntity<ApiResponse<VehicleResponse>> updateVehicle(
            @PathVariable String licensePlate,
            @RequestBody VehicleUpdateRequest request) {
        VehicleResponse updatedVehicle = vehicleService.updateVehicle(licensePlate, request);
        // 返回 200 OK 状态码
        return ResponseEntity.ok(ApiResponse.success(updatedVehicle));
    }

    /**
     * 删除车辆
     * @param licensePlate 要删除的车辆车牌号
     * @return 操作成功消息
     */
    @DeleteMapping("/{licensePlate}")
    public ResponseEntity<ApiResponse<Void>> deleteVehicle(@PathVariable String licensePlate) {
        vehicleService.deleteVehicle(licensePlate);
        // 返回 200 OK 状态码，并携带成功消息
        return ResponseEntity.ok(ApiResponse.success("删除成功"));
    }
}