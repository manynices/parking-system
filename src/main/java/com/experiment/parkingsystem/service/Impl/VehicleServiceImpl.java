package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.VehicleCreateRequest;
import com.experiment.parkingsystem.dto.VehicleResponse;
import com.experiment.parkingsystem.dto.VehicleUpdateRequest;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.VehicleMapper; // 确保导入正确的Mapper
import com.experiment.parkingsystem.service.VehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;

    public VehicleServiceImpl(VehicleMapper vehicleMapper) {
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public VehicleResponse createVehicle(VehicleCreateRequest request) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(request, vehicle);
        vehicle.setVehicleId(generateNewVehicleId()); // 生成新的 Vehicle ID
        vehicleMapper.insert(vehicle);
        return VehicleResponse.fromEntity(vehicle);
    }

    @Override
    public PaginatedResponse<VehicleResponse> getVehicles(int page, int size, String ownerId, String status) {
        PageHelper.startPage(page, size);
        List<Vehicle> vehicles = vehicleMapper.findByCriteria(ownerId, status); // 调用带过滤条件的查询
        PageInfo<Vehicle> pageInfo = new PageInfo<>(vehicles);

        List<VehicleResponse> dtoList = pageInfo.getList().stream()
                .map(VehicleResponse::fromEntity)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public VehicleResponse getVehicleById(String vehicleId) {
        Vehicle vehicle = vehicleMapper.findById(vehicleId);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }
        return VehicleResponse.fromEntity(vehicle);
    }

    @Override
    public VehicleResponse updateVehicle(String vehicleId, VehicleUpdateRequest request) {
        Vehicle existingVehicle = vehicleMapper.findById(vehicleId);
        if (existingVehicle == null) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + vehicleId);
        }

        // --- 标准的部分更新逻辑 ---
        // 只更新请求体中非空（或有文本内容的）字段
        if (StringUtils.hasText(request.getLicensePlate())) {
            existingVehicle.setLicensePlate(request.getLicensePlate());
        }
        if (StringUtils.hasText(request.getVehicleType())) {
            existingVehicle.setVehicleType(request.getVehicleType());
        }
        if (StringUtils.hasText(request.getOwnerId())) {
            existingVehicle.setOwnerId(request.getOwnerId());
        }
        if (StringUtils.hasText(request.getStatus())) {
            existingVehicle.setStatus(request.getStatus());
        }
        // --- 结束部分更新逻辑 ---

        vehicleMapper.update(existingVehicle); // 更新整个实体

        // 返回更新后的完整信息 (重新从数据库查询或直接返回修改后的对象)
        return VehicleResponse.fromEntity(existingVehicle);
    }

    @Override
    public void deleteVehicle(String vehicleId) {
        if (vehicleMapper.existsById(vehicleId) == 0) {
            throw new ResourceNotFoundException("Vehicle not found with id: ".concat(vehicleId));
        }
        vehicleMapper.deleteById(vehicleId);
    }

    // 生成新的车辆ID，例如从 V001, V002...
    private synchronized String generateNewVehicleId() {
        String maxId = vehicleMapper.findMaxVehicleId();
        if (!StringUtils.hasText(maxId)) {
            return "V001";
        }

        String numberString;
        if (maxId.toUpperCase().startsWith("V")) {
            numberString = maxId.substring(1);
        } else {
            numberString = maxId;
        }

        int number = Integer.parseInt(numberString);
        return "V" + String.format("%03d", number + 1);
    }
}