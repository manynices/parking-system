package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.device.*;
import com.experiment.parkingsystem.entity.Device;
import com.experiment.parkingsystem.mapper.DeviceMapper;
import com.experiment.parkingsystem.mapper.ParkingSpaceMapper; // 复用车位Mapper计算统计
import com.experiment.parkingsystem.service.DeviceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;
    private final ParkingSpaceMapper spaceMapper;

    public DeviceServiceImpl(DeviceMapper deviceMapper, ParkingSpaceMapper spaceMapper) {
        this.deviceMapper = deviceMapper;
        this.spaceMapper = spaceMapper;
    }

    // --- ID 转换辅助 ---
    private Long parseId(String idStr, String prefix) {
        if (idStr == null || !idStr.startsWith(prefix)) return null;
        try {
            return Long.parseLong(idStr.substring(prefix.length()));
        } catch (NumberFormatException e) { return null; }
    }

    private String formatId(Long id, String prefix) {
        if (id == null) return null;
        return prefix + String.format("%03d", id);
    }

    private DeviceResponse convertToResponse(Device device) {
        DeviceResponse res = new DeviceResponse();
        BeanUtils.copyProperties(device, res);
        res.setDeviceId(formatId(device.getDeviceId(), "D"));
        return res;
    }

    @Override
    @Transactional
    public DeviceResponse addDevice(DeviceCreateRequest request) {
        Device device = new Device();
        BeanUtils.copyProperties(request, device);
        device.setCreateTime(LocalDateTime.now());
        device.setUpdateTime(LocalDateTime.now());

        deviceMapper.insert(device);

        return convertToResponse(device);
    }

    @Override
    @Transactional
    public DeviceResponse updateDeviceStatus(String deviceIdStr, DeviceStatusUpdateRequest request) {
        Long deviceId = parseId(deviceIdStr, "D");
        Device device = deviceMapper.selectById(deviceId);
        if (device == null) throw new RuntimeException("设备不存在");

        device.setStatus(request.getStatus());
        device.setFaultRemark(request.getFaultRemark());
        device.setUpdateTime(LocalDateTime.now());

        deviceMapper.updateStatus(device);

        return convertToResponse(device);
    }

    @Override
    public PaginatedResponse<DeviceResponse> listDevices(int page, int size, String deviceType, String status, String parkingArea) {
        PageHelper.startPage(page, size);
        List<Device> list = deviceMapper.selectList(deviceType, status, parkingArea);
        PageInfo<Device> pageInfo = new PageInfo<>(list);

        List<DeviceResponse> dtoList = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 适配您的 (total, list) 构造函数
        return new PaginatedResponse<DeviceResponse>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public DeviceParkingStatsResponse getDeviceParkingStats(String deviceIdStr) {
        Long deviceId = parseId(deviceIdStr, "D");
        Device device = deviceMapper.selectById(deviceId);
        if (device == null) throw new RuntimeException("设备不存在");

        String area = device.getParkingArea();
        if (area == null) throw new RuntimeException("该设备未绑定车库区域");

        // 使用 Module 4 的 Mapper 进行统计
        long total = spaceMapper.countByAreaAndStatus(area, null, null);     // 总数
        long free = spaceMapper.countByAreaAndStatus(area, "空闲", null);     // 空闲
        long used = spaceMapper.countByAreaAndStatus(area, "占用", null);     // 占用

        DeviceParkingStatsResponse res = new DeviceParkingStatsResponse();
        res.setDeviceId(deviceIdStr);
        res.setParkingArea(area);
        res.setTotalSpaces(total);
        res.setFreeSpaces(free);
        res.setUsedSpaces(used); // 或者 total - free
        res.setUpdateTime(LocalDateTime.now());

        return res;
    }
}