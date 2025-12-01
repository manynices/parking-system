package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.ChargeRecordCreateRequest;
import com.experiment.parkingsystem.dto.ChargeRecordResponse;
import com.experiment.parkingsystem.entity.ChargeRecord;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.ChargeRecordMapper;
import com.experiment.parkingsystem.mapper.VehicleMapper;
import com.experiment.parkingsystem.service.ChargeRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class ChargeRecordServiceImpl implements ChargeRecordService {

    private final ChargeRecordMapper chargeRecordMapper;
    private final VehicleMapper vehicleMapper; // 注入该 Mapper 以便获取车辆信息

    public ChargeRecordServiceImpl(ChargeRecordMapper chargeRecordMapper, VehicleMapper vehicleMapper) {
        this.chargeRecordMapper = chargeRecordMapper;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public ChargeRecordResponse createChargeRecord(ChargeRecordCreateRequest request) {
        // 1. 检查车辆是否存在，防止外键错误
        Vehicle vehicle = vehicleMapper.findById(request.getVehicleId());
        if (vehicle == null) {
            throw new ResourceNotFoundException("无法创建收费记录：找不到ID为 " + request.getVehicleId() + " 的车辆");
        }

        // 2. 转换 DTO 为实体类
        ChargeRecord record = new ChargeRecord();
        BeanUtils.copyProperties(request, record);
        record.setRecordId(generateNewRecordId()); // 生成新的唯一ID

        // 3. 插入数据库
        chargeRecordMapper.insert(record);

        // 4. 使用车辆的车牌号来构造并返回更详细的响应体
        return ChargeRecordResponse.fromEntity(record, vehicle.getLicensePlate());
    }

    @Override
    public PaginatedResponse<ChargeRecordResponse> getChargeRecords(int page, int size, String licensePlate, String vehicleId, String adminId, Date startTime, Date endTime) {
        PageHelper.startPage(page, size);
        List<ChargeRecordResponse> records = chargeRecordMapper.findByCriteria(licensePlate, vehicleId, adminId, startTime, endTime);
        PageInfo<ChargeRecordResponse> pageInfo = new PageInfo<>(records);

        return new PaginatedResponse<>(pageInfo.getTotal(), pageInfo.getList());
    }

    private synchronized String generateNewRecordId() {
        String maxId = chargeRecordMapper.findMaxRecordId();
        if (!StringUtils.hasText(maxId) || !maxId.startsWith("CR")) {
            return "CR001";
        }
        int number = Integer.parseInt(maxId.substring(2));
        return "CR" + String.format("%03d", number + 1);
    }
}