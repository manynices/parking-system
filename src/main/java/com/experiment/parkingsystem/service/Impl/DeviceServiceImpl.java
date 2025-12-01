package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.entity.Device;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.DeviceMapper;
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

    public DeviceServiceImpl(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Override
    public DeviceResponse createDevice(DeviceCreateRequest request) {
        Device device = new Device();
        BeanUtils.copyProperties(request, device);
        device.setDeviceId(generateNewDeviceId());

        deviceMapper.insert(device);
        // 返回插入后的完整信息（包含数据库自动生成的create_time和update_time）
        return DeviceResponse.fromEntity(deviceMapper.findById(device.getDeviceId()));
    }

    @Override
    public DeviceResponse updateDeviceStatus(String deviceId, DeviceStatusUpdateRequest request) {
        // 1. 确认设备存在
        if (deviceMapper.findById(deviceId) == null) {
            throw new ResourceNotFoundException("找不到ID为 " + deviceId + " 的设备");
        }

        // 2. 构造更新对象
        Device deviceToUpdate = new Device();
        deviceToUpdate.setDeviceId(deviceId);
        deviceToUpdate.setStatus(request.getStatus());
        deviceToUpdate.setFaultRemark(request.getFaultRemark());

        // 3. 执行更新
        deviceMapper.update(deviceToUpdate);

        // 4. 返回更新后的完整信息
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

    @Override
    public DeviceParkingStatsResponse getParkingStats(String deviceId) {
        // 1. 确认设备存在，并获取其关联的车库区域
        Device device = deviceMapper.findById(deviceId);
        if (device == null) {
            throw new ResourceNotFoundException("找不到ID为 " + deviceId + " 的设备");
        }

        // 2. 【模拟数据】在真实系统中，这里会有一套复杂的逻辑：
        //    - 根据 device.getParkingArea() 查询 `parking_space` 表获取总车位数
        //    - 查询 `parking_record` 表或缓存，统计当前在场的车辆数作为已用车位
        //    - 计算出空闲车位数
        //    此处我们返回固定的模拟数据作为演示
        int totalSpaces;
        int usedSpaces;
        if ("B1区".equals(device.getParkingArea())) {
            totalSpaces = 200;
            usedSpaces = 150;
        } else {
            totalSpaces = 180;
            usedSpaces = 90;
        }
        int freeSpaces = totalSpaces - usedSpaces;

        // 3. 构造并返回响应
        return new DeviceParkingStatsResponse(
                deviceId,
                device.getParkingArea(),
                totalSpaces,
                usedSpaces,
                freeSpaces,
                new Date() // 返回当前时间作为更新时间
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