package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.common.UserContext;
import com.experiment.parkingsystem.dto.vehicle.*;
import com.experiment.parkingsystem.entity.User;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.entity.VehicleBindApplication;
import com.experiment.parkingsystem.mapper.UserMapper;
import com.experiment.parkingsystem.mapper.VehicleBindApplicationMapper;
import com.experiment.parkingsystem.mapper.VehicleMapper;
import com.experiment.parkingsystem.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleBindApplicationMapper bindApplicationMapper;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VehicleServiceImpl(VehicleMapper vehicleMapper, VehicleBindApplicationMapper bindApplicationMapper, UserMapper userMapper) {
        this.vehicleMapper = vehicleMapper;
        this.bindApplicationMapper = bindApplicationMapper;
        this.userMapper = userMapper;
    }

    // --- 辅助方法：ID 解析与格式化 ---
    private Long parseId(String idStr, String prefix) {
        if (idStr == null || !idStr.startsWith(prefix)) return null;
        try {
            return Long.parseLong(idStr.substring(prefix.length()));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatId(Long id, String prefix) {
        if (id == null) return null;
        return prefix + String.format("%03d", id);
    }

    // --- 辅助方法：DTO 转换 ---
    private VehicleResponse convertToResponse(Vehicle v) {
        VehicleResponse response = new VehicleResponse();
        BeanUtils.copyProperties(v, response);
        response.setVehicleId(formatId(v.getVehicleId(), "V"));
        response.setOwnerId(formatId(v.getOwnerId(), "O"));
        response.setUserId(formatId(v.getUserId(), "U"));

        // 补全姓名
        if (v.getUserName() == null && v.getUserId() != null) {
            User u = userMapper.selectById(v.getUserId());
            if (u != null) response.setUserName(u.getName());
        }
        if (v.getOwnerName() == null && v.getOwnerId() != null) {
            User u = userMapper.selectById(v.getOwnerId());
            if (u != null) response.setOwnerName(u.getName());
        }

        // 模拟车位和月卡数据
        response.setIsPrivateSpace(false);
        response.setHasMonthlyCard(false);

        return response;
    }

    @Override
    @Transactional
    public VehicleResponse addVehicle(VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(request, vehicle);

        // 解析 String ID 为 Long
        // 假设前端传 ownerId="O001", userId="U001"
        vehicle.setOwnerId(parseId(request.getOwnerId(), "O"));
        vehicle.setUserId(parseId(request.getUserId(), "U"));

        vehicle.setCreateTime(LocalDateTime.now());

        vehicleMapper.insert(vehicle);

        return convertToResponse(vehicle);
    }

    @Override
    @Transactional
    public VehicleBindResponse applyBind(VehicleBindRequest request) {
        Long userId = UserContext.getUserId();

        VehicleBindApplication app = new VehicleBindApplication();
        BeanUtils.copyProperties(request, app);
        app.setUserId(userId);
        app.setStatus("待审核");
        app.setCreateTime(LocalDateTime.now());

        try {
            app.setProofImages(objectMapper.writeValueAsString(request.getProofImages()));
        } catch (Exception e) {
            app.setProofImages("[]");
        }

        bindApplicationMapper.insert(app);

        // 构造正确的响应对象 VehicleBindResponse
        VehicleBindResponse response = new VehicleBindResponse();
        BeanUtils.copyProperties(app, response);
        response.setApplicationId("UVA" + String.format("%03d", app.getApplicationId()));

        return response;
    }

    @Override
    public PaginatedResponse<VehicleResponse> listVehicles(int page, int size, String ownerIdStr, String userIdStr, String status, String vehicleClass, String licensePlate) {
        PageHelper.startPage(page, size);

        // 解析查询参数中的 ID
        Long ownerId = parseId(ownerIdStr, "O");
        if (ownerId == null) ownerId = parseId(ownerIdStr, "U"); // 兼容 Uxxx
        Long userId = parseId(userIdStr, "U");

        List<Vehicle> list = vehicleMapper.selectList(ownerId, userId, status, vehicleClass, licensePlate);
        PageInfo<Vehicle> pageInfo = new PageInfo<>(list);

        List<VehicleResponse> dtoList = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 适配您的 (total, list) 构造函数
        return new PaginatedResponse<VehicleResponse>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public VehicleResponse getVehicleById(String vehicleIdStr) {
        Long id = parseId(vehicleIdStr, "V");
        Vehicle v = vehicleMapper.selectById(id);
        if (v == null) throw new RuntimeException("车辆不存在");
        return convertToResponse(v);
    }

    @Override
    @Transactional
    public VehicleResponse updateVehicle(String vehicleIdStr, VehicleRequest request) {
        Long id = parseId(vehicleIdStr, "V");
        Vehicle v = vehicleMapper.selectById(id);
        if (v == null) throw new RuntimeException("车辆不存在");

        BeanUtils.copyProperties(request, v);
        // ID 不允许修改
        v.setVehicleId(id);
        // 重新设置关联ID
        if (request.getOwnerId() != null) v.setOwnerId(parseId(request.getOwnerId(), "O"));
        if (request.getUserId() != null) v.setUserId(parseId(request.getUserId(), "U"));

        vehicleMapper.updateById(v);
        return convertToResponse(v);
    }

    @Override
    public void deleteVehicle(String vehicleIdStr) {
        Long id = parseId(vehicleIdStr, "V");
        vehicleMapper.deleteById(id);
    }

    @Override
    public VehicleResponse getVehicleByPlate(String licensePlate) {
        Vehicle v = vehicleMapper.selectByLicensePlate(licensePlate);
        if (v == null) throw new RuntimeException("车辆不存在");
        return convertToResponse(v);
    }

    @Override
    public List<VehicleResponse> getMyVehicles() {
        Long userId = UserContext.getUserId();
        List<Vehicle> list = vehicleMapper.selectByUserId(userId);
        return list.stream().map(v -> {
            VehicleResponse res = convertToResponse(v);
            // 模拟数据
            res.setIsPrivateSpace(true);
            res.setPrivateSpaceNo("B1-001");
            res.setHasMonthlyCard(true);
            return res;
        }).collect(Collectors.toList());
    }
}