package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.audit.*;
import com.experiment.parkingsystem.entity.OwnerVerification;
import com.experiment.parkingsystem.entity.User;
import com.experiment.parkingsystem.entity.Vehicle;
import com.experiment.parkingsystem.entity.VehicleBindApplication;
import com.experiment.parkingsystem.mapper.OwnerVerificationMapper;
import com.experiment.parkingsystem.mapper.UserMapper;
import com.experiment.parkingsystem.mapper.VehicleBindApplicationMapper;
import com.experiment.parkingsystem.mapper.VehicleMapper;
import com.experiment.parkingsystem.service.AuditService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditServiceImpl implements AuditService {

    private final OwnerVerificationMapper ownerMapper;
    private final VehicleBindApplicationMapper vehicleBindMapper;
    private final UserMapper userMapper;
    private final VehicleMapper vehicleMapper;

    public AuditServiceImpl(OwnerVerificationMapper ownerMapper,
                            VehicleBindApplicationMapper vehicleBindMapper,
                            UserMapper userMapper,
                            VehicleMapper vehicleMapper) {
        this.ownerMapper = ownerMapper;
        this.vehicleBindMapper = vehicleBindMapper;
        this.userMapper = userMapper;
        this.vehicleMapper = vehicleMapper;
    }

    // --- ID 解析辅助 ---
    private Long parseId(String idStr, String prefix) {
        if (idStr == null || !idStr.startsWith(prefix)) return null;
        try {
            return Long.parseLong(idStr.substring(prefix.length()));
        } catch (NumberFormatException e) { return null; }
    }

    @Override
    public PaginatedResponse<AuditListItemResponse> listPendingAudits(int page, int size, String type) {
        List<AuditListItemResponse> allList = new ArrayList<>();

        // 1. 获取业主认证申请
        if (type == null || "owner-verification".equals(type)) {
            List<OwnerVerification> oaList = ownerMapper.selectPendingList();
            for (OwnerVerification oa : oaList) {
                AuditListItemResponse dto = new AuditListItemResponse();
                dto.setApplicationId("OA" + String.format("%03d", oa.getApplicationId()));
                dto.setType("owner-verification");
                dto.setApplicantName(oa.getName());
                dto.setApplyTime(oa.getCreateTime());
                dto.setStatus(oa.getStatus());
                allList.add(dto);
            }
        }

        // 2. 获取车辆绑定申请
        if (type == null || "vehicle-bind".equals(type)) {
            List<VehicleBindApplication> uvaList = vehicleBindMapper.selectPendingList();
            for (VehicleBindApplication uva : uvaList) {
                AuditListItemResponse dto = new AuditListItemResponse();
                dto.setApplicationId("UVA" + String.format("%03d", uva.getApplicationId()));
                dto.setType("vehicle-bind");

                // 查询申请人姓名
                User u = userMapper.selectById(uva.getUserId());
                dto.setApplicantName(u != null ? u.getName() : "未知用户");

                dto.setLicensePlate(uva.getLicensePlate());
                dto.setApplyTime(uva.getCreateTime());
                dto.setStatus(uva.getStatus());
                allList.add(dto);
            }
        }

        // 3. 内存排序 (按时间)
        allList.sort(Comparator.comparing(AuditListItemResponse::getApplyTime));

        // 4. 内存分页
        int total = allList.size();
        int fromIndex = (page - 1) * size;
        List<AuditListItemResponse> pagedList;

        if (fromIndex >= total) {
            pagedList = new ArrayList<>();
        } else {
            int toIndex = Math.min(fromIndex + size, total);
            pagedList = allList.subList(fromIndex, toIndex);
        }

        // 5. 返回 (符合 long total, List list 构造)
        return new PaginatedResponse<AuditListItemResponse>((long) total, pagedList);
    }

    @Override
    @Transactional
    public AuditReviewResponse approveAudit(String applicationIdStr, AuditReviewRequest request) {
        // 判断类型
        if (applicationIdStr.startsWith("OA")) {
            return approveOwnerVerification(applicationIdStr, request);
        } else if (applicationIdStr.startsWith("UVA")) {
            return approveVehicleBind(applicationIdStr, request);
        } else {
            throw new RuntimeException("无效的申请ID");
        }
    }

    @Override
    @Transactional
    public AuditReviewResponse rejectAudit(String applicationIdStr, AuditReviewRequest request) {
        // 判断类型
        if (applicationIdStr.startsWith("OA")) {
            return rejectOwnerVerification(applicationIdStr, request);
        } else if (applicationIdStr.startsWith("UVA")) {
            return rejectVehicleBind(applicationIdStr, request);
        } else {
            throw new RuntimeException("无效的申请ID");
        }
    }

    // --- 内部逻辑：业主认证 ---

    private AuditReviewResponse approveOwnerVerification(String idStr, AuditReviewRequest req) {
        Long id = parseId(idStr, "OA");
        OwnerVerification ov = ownerMapper.selectById(id);
        if (ov == null) throw new RuntimeException("申请不存在");

        // 1. 更新申请状态
        ov.setStatus("通过");
        ov.setVerifierName("管理员"); // 这里简化，实际应获取当前管理员名
        ov.setRemark(req.getRemark());
        ov.setVerifiedTime(LocalDateTime.now());
        ownerMapper.updateStatus(ov);

        // 2. 【副作用】更新用户表，设置为业主
        User user = userMapper.selectById(ov.getUserId());
        if (user != null) {
            user.setIsOwnerVerified(true);
            user.setName(ov.getName()); // 同步真实姓名
            user.setIdCard(ov.getIdCard());
            userMapper.updateById(user);
        }

        return buildResponse(idStr, "通过", req.getRemark());
    }

    private AuditReviewResponse rejectOwnerVerification(String idStr, AuditReviewRequest req) {
        Long id = parseId(idStr, "OA");
        OwnerVerification ov = ownerMapper.selectById(id);
        if (ov == null) throw new RuntimeException("申请不存在");

        ov.setStatus("拒绝");
        ov.setVerifierName("管理员");
        ov.setRemark(req.getRemark());
        ov.setVerifiedTime(LocalDateTime.now());
        ownerMapper.updateStatus(ov);

        return buildResponse(idStr, "拒绝", req.getRemark());
    }

    // --- 内部逻辑：车辆绑定 ---

    private AuditReviewResponse approveVehicleBind(String idStr, AuditReviewRequest req) {
        Long id = parseId(idStr, "UVA");
        VehicleBindApplication uva = vehicleBindMapper.selectById(id);
        if (uva == null) throw new RuntimeException("申请不存在");

        // 1. 更新申请状态
        uva.setStatus("通过");
        uva.setRemark(req.getRemark());
        vehicleBindMapper.updateStatus(uva);

        // 2. 【副作用】绑定或创建车辆
        Vehicle vehicle = vehicleMapper.selectByLicensePlate(uva.getLicensePlate());
        if (vehicle == null) {
            // 车辆不存在，新建并绑定
            vehicle = new Vehicle();
            vehicle.setLicensePlate(uva.getLicensePlate());
            vehicle.setVehicleType(uva.getVehicleType());
            vehicle.setVehicleBrand(uva.getVehicleBrand());
            vehicle.setVehicleColor(uva.getVehicleColor());
            vehicle.setUserId(uva.getUserId()); // 绑定给申请人
            vehicle.setVehicleClass("业主车辆"); // 默认为业主车辆
            vehicle.setStatus("在用");
            vehicle.setCreateTime(LocalDateTime.now());
            vehicleMapper.insert(vehicle);
        } else {
            // 车辆已存在，更新驾驶人/所有人
            vehicle.setUserId(uva.getUserId());
            vehicleMapper.updateById(vehicle);
        }

        return buildResponse(idStr, "通过", req.getRemark());
    }

    private AuditReviewResponse rejectVehicleBind(String idStr, AuditReviewRequest req) {
        Long id = parseId(idStr, "UVA");
        VehicleBindApplication uva = vehicleBindMapper.selectById(id);
        if (uva == null) throw new RuntimeException("申请不存在");

        uva.setStatus("拒绝");
        uva.setRemark(req.getRemark());
        vehicleBindMapper.updateStatus(uva);

        return buildResponse(idStr, "拒绝", req.getRemark());
    }

    private AuditReviewResponse buildResponse(String id, String status, String remark) {
        AuditReviewResponse res = new AuditReviewResponse();
        res.setApplicationId(id);
        res.setStatus(status);
        res.setReviewTime(LocalDateTime.now());
        res.setRemark(remark);
        return res;
    }
}