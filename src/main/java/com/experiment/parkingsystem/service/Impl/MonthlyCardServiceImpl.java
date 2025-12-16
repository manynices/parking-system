package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.common.UserContext;
import com.experiment.parkingsystem.dto.monthlycard.*;
import com.experiment.parkingsystem.entity.MonthlyCard;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.mapper.MonthlyCardMapper;
import com.experiment.parkingsystem.mapper.VehicleMapper;
import com.experiment.parkingsystem.service.MonthlyCardService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyCardServiceImpl implements MonthlyCardService {

    private final MonthlyCardMapper cardMapper;
    private final VehicleMapper vehicleMapper;

    public MonthlyCardServiceImpl(MonthlyCardMapper cardMapper, VehicleMapper vehicleMapper) {
        this.cardMapper = cardMapper;
        this.vehicleMapper = vehicleMapper;
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

    private MonthlyCardResponse convertToResponse(MonthlyCard card) {
        MonthlyCardResponse res = new MonthlyCardResponse();
        BeanUtils.copyProperties(card, res);
        res.setCardId(formatId(card.getCardId(), "MC"));
        res.setVehicleId(formatId(card.getVehicleId(), "V"));

        if (card.getVehiclePlate() != null) res.setVehiclePlate(card.getVehiclePlate());
        if (card.getOwnerName() != null) res.setOwnerName(card.getOwnerName());

        if (res.getVehiclePlate() == null) {
            Vehicle v = vehicleMapper.selectById(card.getVehicleId());
            if (v != null) res.setVehiclePlate(v.getLicensePlate());
        }

        return res;
    }

    private MonthlyCardPackageResponse getPackageInfo(String type) {
        if ("月卡".equals(type)) return new MonthlyCardPackageResponse("月卡", 1, new BigDecimal("300.00"), "");
        if ("季卡".equals(type)) return new MonthlyCardPackageResponse("季卡", 3, new BigDecimal("850.00"), "");
        if ("年卡".equals(type)) return new MonthlyCardPackageResponse("年卡", 12, new BigDecimal("3200.00"), "");
        throw new RuntimeException("未知套餐类型");
    }

    @Override
    @Transactional
    public MonthlyCardResponse createCard(MonthlyCardRequest request) {
        Long userId = UserContext.getUserId();
        Long vehicleId = parseId(request.getVehicleId(), "V");

        Vehicle v = vehicleMapper.selectById(vehicleId);
        if (v == null) throw new RuntimeException("车辆不存在");

        MonthlyCardPackageResponse pkg = getPackageInfo(request.getPackageType());
        LocalDateTime start = request.getStartDate().atStartOfDay();
        LocalDateTime end = start.plusMonths(pkg.getDurationMonths()).minusSeconds(1);

        MonthlyCard card = new MonthlyCard();
        BeanUtils.copyProperties(request, card);
        card.setVehicleId(vehicleId);
        card.setIssueTime(LocalDateTime.now());
        card.setValidityPeriod(end);
        card.setStatus("正常");
        card.setCreateTime(LocalDateTime.now());

        cardMapper.insert(card);
        return convertToResponse(card);
    }

    @Override
    @Transactional
    public MonthlyCardResponse renewCard(String cardIdStr, MonthlyCardRenewRequest request) {
        Long cardId = parseId(cardIdStr, "MC");
        MonthlyCard card = cardMapper.selectById(cardId);
        if (card == null) throw new RuntimeException("月卡不存在");

        MonthlyCardPackageResponse pkg = getPackageInfo(request.getPackageType());
        LocalDateTime currentExpiry = card.getValidityPeriod();
        LocalDateTime baseTime = currentExpiry.isBefore(LocalDateTime.now()) ? LocalDateTime.now() : currentExpiry;
        LocalDateTime newExpiry = baseTime.plusMonths(pkg.getDurationMonths());
        newExpiry = newExpiry.with(LocalTime.MAX);

        card.setPackageType(request.getPackageType());
        card.setPaymentMethod(request.getPaymentMethod());
        card.setAmount(pkg.getPrice());
        card.setValidityPeriod(newExpiry);
        card.setRenewTime(LocalDateTime.now());

        cardMapper.updateById(card);

        MonthlyCardResponse res = convertToResponse(card);
        res.setNewValidityPeriod(newExpiry);
        res.setAmount(pkg.getPrice());
        return res;
    }

    @Override
    public MonthlyCardResponse updateStatus(String cardIdStr, MonthlyCardStatusRequest request) {
        Long cardId = parseId(cardIdStr, "MC");
        MonthlyCard card = cardMapper.selectById(cardId);
        if (card == null) throw new RuntimeException("月卡不存在");

        card.setStatus(request.getStatus());
        cardMapper.updateById(card);
        return convertToResponse(card);
    }

    // --- 重点修改的方法：listCards ---
    @Override
    public PaginatedResponse<MonthlyCardResponse> listCards(int page, int size, String vehicleIdStr, String status, String ownerIdStr, String userIdStr) {
        PageHelper.startPage(page, size);

        Long vehicleId = parseId(vehicleIdStr, "V");
        Long ownerId = parseId(ownerIdStr, "O");
        if (ownerId == null) ownerId = parseId(ownerIdStr, "U");
        Long userId = parseId(userIdStr, "U");

        List<MonthlyCard> list = cardMapper.selectList(vehicleId, status, ownerId, userId);
        PageInfo<MonthlyCard> pageInfo = new PageInfo<>(list);

        List<MonthlyCardResponse> dtoList = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 【修改点】
        // 1. 显式指定泛型 <MonthlyCardResponse> 解决 "无法推断类型参数"
        // 2. 参数顺序改为 (total, list) 以匹配你的 PaginatedResponse 定义
        return new PaginatedResponse<MonthlyCardResponse>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public MonthlyCardResponse getCardById(String cardIdStr) {
        Long cardId = parseId(cardIdStr, "MC");
        MonthlyCard card = cardMapper.selectById(cardId);
        if (card == null) throw new RuntimeException("月卡不存在");
        return convertToResponse(card);
    }

    @Override
    public List<MonthlyCardPackageResponse> getPackages() {
        return Arrays.asList(
                new MonthlyCardPackageResponse("月卡", 1, new BigDecimal("300.00"), "月卡套餐，有效期30天"),
                new MonthlyCardPackageResponse("季卡", 3, new BigDecimal("850.00"), "季卡套餐，有效期90天，优惠50元"),
                new MonthlyCardPackageResponse("年卡", 12, new BigDecimal("3200.00"), "年卡套餐，有效期365天，优惠400元")
        );
    }
}