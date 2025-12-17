package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.administrator.*;
import com.experiment.parkingsystem.entity.Administrator;
import com.experiment.parkingsystem.mapper.AdministratorMapper;
import com.experiment.parkingsystem.service.AdministratorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorMapper adminMapper;

    public AdministratorServiceImpl(AdministratorMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    // --- ID 辅助 ---
    private Long parseId(String idStr) {
        if (idStr == null || !idStr.startsWith("A")) return null;
        try {
            return Long.parseLong(idStr.substring(1));
        } catch (NumberFormatException e) { return null; }
    }
    private String formatId(Long id) {
        if (id == null) return null;
        return "A" + String.format("%03d", id);
    }

    private AdminResponse convertToResponse(Administrator admin) {
        AdminResponse res = new AdminResponse();
        BeanUtils.copyProperties(admin, res);
        res.setAdminId(formatId(admin.getAdminId()));
        return res;
    }

    @Override
    @Transactional
    public AdminResponse createAdmin(AdminCreateRequest request) {
        // 1. 检查账号重复
        if (adminMapper.selectByAccount(request.getAccount()) != null) {
            throw new RuntimeException("管理员账号已存在");
        }

        Administrator admin = new Administrator();
        BeanUtils.copyProperties(request, admin);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        // 注意：实际生产需加密密码

        adminMapper.insert(admin);
        return convertToResponse(admin);
    }

    @Override
    public AdminLoginResponse login(AdminLoginRequest request) {
        Administrator admin = adminMapper.selectByAccount(request.getAccount());
        if (admin == null || !admin.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }

        AdminLoginResponse res = new AdminLoginResponse();
        BeanUtils.copyProperties(admin, res);
        res.setAdminId(formatId(admin.getAdminId()));
        // 生成模拟 Token
        res.setToken("ADMIN_JWT_TOKEN_" + admin.getAdminId());
        return res;
    }

    @Override
    public PaginatedResponse<AdminResponse> listAdmins(int page, int size, String name) {
        PageHelper.startPage(page, size);
        List<Administrator> list = adminMapper.selectList(name);
        PageInfo<Administrator> pageInfo = new PageInfo<>(list);

        List<AdminResponse> dtoList = list.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // 适配您的 (total, list) 构造函数
        return new PaginatedResponse<AdminResponse>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public AdminResponse getAdminById(String adminIdStr) {
        Long id = parseId(adminIdStr);
        Administrator admin = adminMapper.selectById(id);
        if (admin == null) throw new RuntimeException("管理员不存在");
        return convertToResponse(admin);
    }

    @Override
    @Transactional
    public AdminResponse updateAdmin(String adminIdStr, AdminUpdateRequest request) {
        Long id = parseId(adminIdStr);
        Administrator admin = adminMapper.selectById(id);
        if (admin == null) throw new RuntimeException("管理员不存在");

        if (request.getName() != null) admin.setName(request.getName());
        if (request.getPermissionLevel() != null) admin.setPermissionLevel(request.getPermissionLevel());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            admin.setPassword(request.getPassword());
        }
        admin.setUpdateTime(LocalDateTime.now());

        adminMapper.update(admin);
        return convertToResponse(admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(String adminIdStr) {
        Long id = parseId(adminIdStr);
        // 不能删除自己，或者检查权限（这里省略，实际项目中需要校验）
        adminMapper.deleteById(id);
    }
}