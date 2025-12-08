package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.entity.Device;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.DeviceMapper;
import com.experiment.parkingsystem.mapper.ParkingSpaceMapper; // 【新增】导入 ParkingSpaceMapper
import com.experiment.parkingsystem.service.DeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;
    private final ParkingSpaceMapper parkingSpaceMapper; // 【新增】注入 ParkingSpaceMapper

    // 【修改】更新构造函数以接收新的 Mapper
    public DeviceServiceImpl(DeviceMapper deviceMapper, ParkingSpaceMapper parkingSpaceMapper) {
        this.deviceMapper = deviceMapper;
        this.parkingSpaceMapper = parkingSpaceMapper;
    }

    @Override
    public DeviceResponse createDevice(DeviceCreateRequest request) {
        Device device = new Device();
        BeanUtils.copyProperties(request, device);
        device.setDeviceId(generateNewDeviceId());

        deviceMapper.insert(device);
        return DeviceResponse.fromEntity(deviceMapper.findById(device.getDeviceId()));
    }

    @Override
    public DeviceResponse updateDeviceStatus(String deviceId, DeviceStatusUpdateRequest request) {
        if (deviceMapper.findById(deviceId) == null) {
            throw new ResourceNotFoundException("找不到ID为 " + deviceId + " 的设备");
        }

        Device deviceToUpdate = new Device();
        deviceToUpdate.setDeviceId(deviceId);
        deviceToUpdate.setStatus(request.getStatus());
        deviceToUpdate.setFaultRemark(request.getFaultRemark());

        deviceMapper.update(deviceToUpdate);
        return DeviceResponse.fromEntity(deviceMapper.findById(deviceId));
    }

    @Override
    public PaginatedResponse<DeviceResponse> getDevices(int page, int size, String deviceType, String status, String parkingArea) {
        PageHelper.startPage(page, size);
        List<Device> devices = deviceMapper.findByCriteria(deviceType, status, parkingArea);
        PageInfo<Device> pageInfo = new PageInfo<>(devices);

        List<DeviceResponse> dtoList = pageInfo.getList().stream()
                .map(DeviceResponse::fromEntity)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(pageInfo.getTotal(), dtoList);
    }

    /**
     * 【重点修改】获取设备关联的车位统计信息 (真实数据版)
     */
    @Override
    public DeviceParkingStatsResponse getParkingStats(String deviceId) {
        // 1. 确认设备存在，并获取其关联的车库区域
        Device device = deviceMapper.findById(deviceId);
        if (device == null) {
            throw new ResourceNotFoundException("找不到ID为 " + deviceId + " 的设备");
        }
        String parkingArea = device.getParkingArea();

        // 2. 【真实逻辑】通过 ParkingSpaceMapper 查询数据库
        //    - 查询已用车位
        long usedSpaces = parkingSpaceMapper.countByAreaAndStatus(parkingArea, "占用");
        //    - 查询空闲车位
        long freeSpaces = parkingSpaceMapper.countByAreaAndStatus(parkingArea, "空闲");
        //    - 计算总车位
        long totalSpaces = usedSpaces + freeSpaces;

        // 3. 构造并返回响应 (注意 long 转 Integer)
        return new DeviceParkingStatsResponse(
                deviceId,
                parkingArea,
                (int) totalSpaces,
                (int) usedSpaces,
                (int) freeSpaces,
                new Date() // 返回当前时间作为统计更新时间
        );
    }

    private synchronized String generateNewDeviceId() {
        String maxId = deviceMapper.findMaxDeviceId();
        if (!StringUtils.hasText(maxId) || !maxId.startsWith("D")) {
            return "D001";
        }
        int number = Integer.parseInt(maxId.substring(1));
        return "D" + String.format("%03d", number + 1);
    }
}