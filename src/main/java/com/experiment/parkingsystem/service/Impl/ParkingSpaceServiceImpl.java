package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.UserContext;
import com.experiment.parkingsystem.dto.parkingspace.*;
import com.experiment.parkingsystem.entity.ParkingSpace;
import com.experiment.parkingsystem.entity.User;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.mapper.ParkingSpaceMapper;
import com.experiment.parkingsystem.mapper.UserMapper;
import com.experiment.parkingsystem.mapper.VehicleMapper;
import com.experiment.parkingsystem.service.ParkingSpaceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceMapper spaceMapper;
    private final VehicleMapper vehicleMapper;
    private final UserMapper userMapper;

    public ParkingSpaceServiceImpl(ParkingSpaceMapper spaceMapper, VehicleMapper vehicleMapper, UserMapper userMapper) {
        this.spaceMapper = spaceMapper;
        this.vehicleMapper = vehicleMapper;
        this.userMapper = userMapper;
    }

    // --- 辅助方法 ---
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

    private ParkingSpaceItemResponse convertToItem(ParkingSpace ps) {
        ParkingSpaceItemResponse item = new ParkingSpaceItemResponse();
        BeanUtils.copyProperties(ps, item);
        item.setVehicleId(formatId(ps.getVehicleId(), "V"));

        // 补充信息
        item.setHourlyRate(new BigDecimal("4.00"));
        item.setDailyMax(new BigDecimal("20.00"));
        if ("私人车位".equals(ps.getSpaceType())) {
            item.setDescription("私人车位，买断制，购买后免费停车");
        }

        // 4.6 接口特定字段映射
        item.setLicensePlate(ps.getVehiclePlate());

        return item;
    }

    @Override
    public ParkingSpaceStatsResponse getParkingStats(String parkingArea, String status, String spaceNo, String spaceType) {
        List<ParkingSpace> list = spaceMapper.selectList(parkingArea, status, spaceNo, spaceType, null);
        long total = spaceMapper.countByAreaAndStatus(parkingArea, null, spaceType);
        long free = spaceMapper.countByAreaAndStatus(parkingArea, "空闲", spaceType);
        long occupied = spaceMapper.countByAreaAndStatus(parkingArea, "占用", spaceType);

        ParkingSpaceStatsResponse res = new ParkingSpaceStatsResponse();
        res.setParkingArea(parkingArea);
        res.setTotal(total);
        res.setFree(free);
        res.setOccupied(occupied);
        res.setSpaceList(list.stream().map(this::convertToItem).collect(Collectors.toList()));
        return res;
    }

    @Override
    @Transactional
    public ParkingSpaceItemResponse updateSpaceStatus(String spaceNo, ParkingSpaceStatusUpdateRequest request) {
        ParkingSpace space = spaceMapper.selectBySpaceNo(spaceNo);
        if (space == null) throw new RuntimeException("车位不存在");

        space.setStatus(request.getStatus());
        space.setVehicleId(parseId(request.getVehicleId(), "V"));
        space.setUpdateTime(LocalDateTime.now());

        spaceMapper.updateStatus(space);

        // 重新查询以获取最新关联信息
        return convertToItem(spaceMapper.selectBySpaceNo(spaceNo));
    }

    @Override
    public ParkingSpaceAvailableResponse getAvailableSpaces(String parkingArea, String spaceType) {
        if (spaceType == null) spaceType = "公共车位"; // 默认

        List<ParkingSpace> list = spaceMapper.selectList(parkingArea, "空闲", null, spaceType, null);
        long total = spaceMapper.countByAreaAndStatus(parkingArea, null, spaceType);
        long available = spaceMapper.countByAreaAndStatus(parkingArea, "空闲", spaceType);

        ParkingSpaceAvailableResponse res = new ParkingSpaceAvailableResponse();
        res.setParkingArea(parkingArea);
        res.setTotal(total);
        res.setAvailable(available);
        res.setSpaceList(list.stream().map(this::convertToItem).collect(Collectors.toList()));
        return res;
    }

    @Override
    public ParkingSpaceSalesResponse getPrivateSpacesForSale(String parkingArea) {
        List<ParkingSpace> list = spaceMapper.selectList(parkingArea, "空闲", null, "私人车位", null);
        long total = spaceMapper.countByAreaAndStatus(parkingArea, null, "私人车位");
        long available = list.size();

        ParkingSpaceSalesResponse res = new ParkingSpaceSalesResponse();
        res.setTotal(total);
        res.setAvailable(available);
        res.setPrice(new BigDecimal("10000.00")); // 固定价格
        res.setSpaceList(list.stream().map(this::convertToItem).collect(Collectors.toList()));
        return res;
    }

    @Override
    @Transactional
    public ParkingSpacePurchaseResponse purchaseSpace(ParkingSpacePurchaseRequest request) {
        Long userId = UserContext.getUserId();

        // 1. 校验车位
        ParkingSpace space = spaceMapper.selectBySpaceNo(request.getSpaceNo());
        if (space == null) throw new RuntimeException("车位不存在");
        if (!"私人车位".equals(space.getSpaceType())) throw new RuntimeException("非私人车位不可购买");
        if (space.getOwnerId() != null) throw new RuntimeException("车位已被购买");

        // 2. 校验用户 (必须是业主)
        User user = userMapper.selectById(userId);
        if (user == null || !Boolean.TRUE.equals(user.getIsOwnerVerified())) {
            throw new RuntimeException("非认证业主不可购买车位");
        }

        // 3. 执行购买
        Long vehicleId = parseId(request.getVehicleId(), "V");
        Vehicle vehicle = vehicleMapper.selectById(vehicleId);

        space.setOwnerId(userId);
        space.setPurchaseTime(LocalDateTime.now());
        space.setPurchaseAmount(new BigDecimal("10000.00"));
        space.setPaymentMethod(request.getPaymentMethod());
        space.setStatus("占用"); // 购买后状态变为占用？或者根据业务逻辑，这里设置为文档返回的"已购买"状态
        // 文档示例 Response 返回 status: "已购买"。
        // 但数据库 status 字段通常是枚举 (空闲/占用)。
        // 这里为了匹配文档响应，我们将数据库更新为 '占用' (表示有主了)，但返回给前端 DTO 设为 '已购买'。
        space.setStatus("占用");
        space.setUpdateTime(LocalDateTime.now());

        // 如果绑定了车，更新关联
        if (vehicleId != null) {
            space.setVehicleId(vehicleId);
        }

        spaceMapper.updatePurchaseInfo(space);

        // 4. 构造响应
        ParkingSpacePurchaseResponse res = new ParkingSpacePurchaseResponse();
        res.setSpaceNo(space.getSpaceNo());
        res.setVehicleId(formatId(vehicleId, "V"));
        if (vehicle != null) res.setLicensePlate(vehicle.getLicensePlate());
        res.setOwnerName(user.getName());
        res.setAmount(space.getPurchaseAmount());
        res.setPaymentMethod(space.getPaymentMethod());
        res.setPurchaseTime(space.getPurchaseTime());
        res.setStatus("已购买"); // 强制符合文档

        return res;
    }

    @Override
    public List<ParkingSpaceItemResponse> getMyPrivateSpaces() {
        Long userId = UserContext.getUserId();
        List<ParkingSpace> list = spaceMapper.selectList(null, null, null, "私人车位", userId);
        return list.stream().map(this::convertToItem).collect(Collectors.toList());
    }
}