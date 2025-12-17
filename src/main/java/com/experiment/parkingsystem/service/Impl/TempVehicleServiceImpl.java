package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.tempvehicle.*;
import com.experiment.parkingsystem.entity.TempVehicle;
import com.experiment.parkingsystem.mapper.TempVehicleMapper;
import com.experiment.parkingsystem.service.TempVehicleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TempVehicleServiceImpl implements TempVehicleService {

    private final TempVehicleMapper tempVehicleMapper;

    public TempVehicleServiceImpl(TempVehicleMapper tempVehicleMapper) {
        this.tempVehicleMapper = tempVehicleMapper;
    }

    private String formatId(Long id, String prefix) {
        if (id == null) return null;
        return prefix + String.format("%03d", id);
    }

    @Override
    @Transactional
    public TempVehicleRegisterResponse register(TempVehicleRegisterRequest request) {
        TempVehicle vehicle = new TempVehicle();
        BeanUtils.copyProperties(request, vehicle);
        vehicle.setStatus("待入场");
        vehicle.setCreateTime(LocalDateTime.now());

        tempVehicleMapper.insert(vehicle);

        TempVehicleRegisterResponse response = new TempVehicleRegisterResponse();
        BeanUtils.copyProperties(vehicle, response);
        response.setRegistrationId(formatId(vehicle.getRegistrationId(), "TR"));
        return response;
    }

    @Override
    @Transactional
    public TempVehicleEntryResponse recordEntry(TempVehicleEntryRequest request) {
        // 查找最近一条待入场记录，如果没有则可能是直接入场(需要新建记录)
        TempVehicle vehicle = tempVehicleMapper.selectLatestActiveByPlate(request.getLicensePlate());

        if (vehicle == null) {
            // 如果没有预约记录，自动创建一条新的 "在场" 记录
            vehicle = new TempVehicle();
            vehicle.setLicensePlate(request.getLicensePlate());
            vehicle.setVehicleType("临时车辆");
            vehicle.setCreateTime(LocalDateTime.now());
            // 先插入获取ID
            vehicle.setStatus("待入场"); // 临时状态，下面会更新
            tempVehicleMapper.insert(vehicle);
        }

        vehicle.setEntryTime(request.getEntryTime());
        vehicle.setEntryDeviceId(request.getDeviceId());
        vehicle.setEntryImage(request.getImageUrl());
        vehicle.setRemark(request.getRemark());
        vehicle.setStatus("在场");

        tempVehicleMapper.updateEntry(vehicle);

        TempVehicleEntryResponse response = new TempVehicleEntryResponse();
        BeanUtils.copyProperties(vehicle, response);
        response.setRegistrationId(formatId(vehicle.getRegistrationId(), "TR"));
        return response;
    }

    @Override
    @Transactional
    public TempVehicleExitResponse recordExit(TempVehicleExitRequest request) {
        TempVehicle vehicle = tempVehicleMapper.selectLatestActiveByPlate(request.getLicensePlate());

        if (vehicle == null || !"在场".equals(vehicle.getStatus())) {
            throw new RuntimeException("该车辆未入场或状态异常");
        }

        vehicle.setExitTime(request.getExitTime());
        vehicle.setExitDeviceId(request.getDeviceId());
        vehicle.setExitImage(request.getImageUrl());
        vehicle.setParkingDuration(request.getParkingDuration());
        vehicle.setParkingFee(request.getParkingFee());
        vehicle.setPaymentMethod(request.getPaymentMethod());
        vehicle.setStatus("已离场");

        tempVehicleMapper.updateExit(vehicle);

        TempVehicleExitResponse response = new TempVehicleExitResponse();
        BeanUtils.copyProperties(vehicle, response);
        response.setRegistrationId(formatId(vehicle.getRegistrationId(), "TR"));
        return response;
    }

    @Override
    public PaginatedResponse<TempVehicleListItemResponse> listRecords(int page, int size, String licensePlate, String status, String startDate, String endDate) {
        PageHelper.startPage(page, size);
        List<TempVehicle> list = tempVehicleMapper.selectList(licensePlate, status, startDate, endDate);
        PageInfo<TempVehicle> pageInfo = new PageInfo<>(list);

        List<TempVehicleListItemResponse> dtoList = list.stream().map(v -> {
            TempVehicleListItemResponse dto = new TempVehicleListItemResponse();
            BeanUtils.copyProperties(v, dto);
            dto.setRegistrationId(formatId(v.getRegistrationId(), "TR"));
            return dto;
        }).collect(Collectors.toList());

        // 适配您的 (total, list) 构造函数
        return new PaginatedResponse<TempVehicleListItemResponse>(pageInfo.getTotal(), dtoList);
    }
}