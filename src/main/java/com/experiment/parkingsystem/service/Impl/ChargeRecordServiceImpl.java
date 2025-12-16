package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.chargerecord.*;
import com.experiment.parkingsystem.entity.ChargeRecord;
import com.experiment.parkingsystem.entity.ParkingSpace;
import com.experiment.parkingsystem.entity.User;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.mapper.ChargeRecordMapper;
import com.experiment.parkingsystem.mapper.ParkingSpaceMapper;
import com.experiment.parkingsystem.mapper.UserMapper;
import com.experiment.parkingsystem.mapper.VehicleMapper;
import com.experiment.parkingsystem.service.ChargeRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChargeRecordServiceImpl implements ChargeRecordService {

    private final ChargeRecordMapper recordMapper;
    private final VehicleMapper vehicleMapper;
    private final UserMapper userMapper;
    private final ParkingSpaceMapper spaceMapper;

    public ChargeRecordServiceImpl(ChargeRecordMapper recordMapper, VehicleMapper vehicleMapper, UserMapper userMapper, ParkingSpaceMapper spaceMapper) {
        this.recordMapper = recordMapper;
        this.vehicleMapper = vehicleMapper;
        this.userMapper = userMapper;
        this.spaceMapper = spaceMapper;
    }

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

    private FeeCalculateResponse doCalculate(String licensePlate, LocalDateTime enter, LocalDateTime exit) {
        FeeCalculateResponse res = new FeeCalculateResponse();
        res.setLicensePlate(licensePlate);
        res.setEnterTime(enter);
        res.setExitTime(exit);

        long minutes = Duration.between(enter, exit).toMinutes();
        res.setParkingDuration(minutes);

        Vehicle vehicle = vehicleMapper.selectByLicensePlate(licensePlate);

        boolean isOwner = false;
        boolean hasPrivateSpace = false;

        if (vehicle != null) {
            if (vehicle.getOwnerId() != null) {
                User owner = userMapper.selectById(vehicle.getOwnerId());
                if (owner != null && Boolean.TRUE.equals(owner.getIsOwnerVerified())) {
                    isOwner = true;
                }
            }
            List<ParkingSpace> spaces = spaceMapper.selectList(null, null, null, "私人车位", null);
            hasPrivateSpace = spaces.stream().anyMatch(s -> s.getVehicleId() != null && s.getVehicleId().equals(vehicle.getVehicleId()));
        }

        res.setIsOwner(isOwner);
        res.setHasPrivateSpace(hasPrivateSpace);
        res.setVehicleClass(isOwner ? "业主车辆" : "临时车辆");

        if (hasPrivateSpace) {
            res.setBaseAmount(BigDecimal.ZERO);
            res.setDiscount(BigDecimal.ZERO);
            res.setTotalAmount(BigDecimal.ZERO);
            res.setOwnerDiscount(BigDecimal.ZERO);
            return res;
        }

        if (minutes <= 60) {
            res.setBaseAmount(BigDecimal.ZERO);
            res.setDiscount(BigDecimal.ZERO);
            res.setTotalAmount(BigDecimal.ZERO);
            res.setOwnerDiscount(BigDecimal.ZERO);
            return res;
        }

        long billableMinutes = minutes - 60;
        double hours = Math.ceil(billableMinutes / 60.0);
        BigDecimal base = new BigDecimal(hours).multiply(new BigDecimal("4.00"));

        if (base.compareTo(new BigDecimal("20.00")) > 0) {
            base = new BigDecimal("20.00");
        }
        res.setBaseAmount(base);

        if (isOwner) {
            BigDecimal finalAmt = base.multiply(new BigDecimal("0.80"));
            BigDecimal discount = base.subtract(finalAmt);
            res.setTotalAmount(finalAmt);
            res.setDiscount(discount);
            res.setOwnerDiscount(discount);
        } else {
            res.setTotalAmount(base);
            res.setDiscount(BigDecimal.ZERO);
            res.setOwnerDiscount(BigDecimal.ZERO);
        }

        return res;
    }

    @Override
    @Transactional
    public ChargeRecordCreateResponse createRecord(ChargeRecordCreateRequest request) {
        Long vehicleId = parseId(request.getVehicleId(), "V");
        Vehicle vehicle = vehicleMapper.selectById(vehicleId);
        if (vehicle == null) throw new RuntimeException("车辆不存在");

        FeeCalculateResponse calc = doCalculate(vehicle.getLicensePlate(), request.getEnterTime(), request.getExitTime());

        ChargeRecord record = new ChargeRecord();
        record.setVehicleId(vehicleId);
        record.setLicensePlate(vehicle.getLicensePlate());
        record.setEnterTime(request.getEnterTime());
        record.setExitTime(request.getExitTime());
        record.setAmount(request.getAmount());
        record.setPaymentMethod(request.getPaymentMethod());
        record.setActualAmount(calc.getTotalAmount());
        record.setDiscount(calc.getDiscount());
        record.setCreateTime(LocalDateTime.now());
        record.setAdminName("Admin");

        recordMapper.insert(record);

        ChargeRecordCreateResponse res = new ChargeRecordCreateResponse();
        BeanUtils.copyProperties(record, res);
        res.setRecordId(formatId(record.getRecordId(), "CR"));
        res.setVehicleId(formatId(record.getVehicleId(), "V"));
        return res;
    }

    // --- 【修改点】listRecords 方法 ---
    @Override
    public PaginatedResponse<ChargeRecordListItemResponse> listRecords(int page, int size, String licensePlate, String vehicleIdStr, String startTime, String endTime, String ownerIdStr, String userIdStr) {
        PageHelper.startPage(page, size);

        Long vehicleId = parseId(vehicleIdStr, "V");
        Long ownerId = parseId(ownerIdStr, "O");
        if (ownerId == null) ownerId = parseId(ownerIdStr, "U");
        Long userId = parseId(userIdStr, "U");

        List<ChargeRecord> list = recordMapper.selectList(licensePlate, vehicleId, startTime, endTime, ownerId, userId);
        PageInfo<ChargeRecord> pageInfo = new PageInfo<>(list);

        List<ChargeRecordListItemResponse> dtoList = list.stream().map(r -> {
            ChargeRecordListItemResponse dto = new ChargeRecordListItemResponse();
            BeanUtils.copyProperties(r, dto);
            dto.setRecordId(formatId(r.getRecordId(), "CR"));
            dto.setVehicleId(formatId(r.getVehicleId(), "V"));
            return dto;
        }).collect(Collectors.toList());

        // 【修改点】
        // 1. 显式指定泛型 <ChargeRecordListItemResponse>
        // 2. 参数顺序调整为 (total, list)
        return new PaginatedResponse<ChargeRecordListItemResponse>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public FeeCalculateResponse calculateFee(FeeCalculateRequest request) {
        return doCalculate(request.getLicensePlate(), request.getEnterTime(), request.getExitTime());
    }
}