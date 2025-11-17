package com.experiment.parkingsystem.service.Impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;
import com.experiment.parkingsystem.entity.Administrator;
import com.experiment.parkingsystem.exception.AuthenticationException;
import com.experiment.parkingsystem.exception.ResourceNotFoundException;
import com.experiment.parkingsystem.mapper.AdministratorMapper;
import com.experiment.parkingsystem.service.AdministratorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdministratorServiceImpl implements AdministratorService {

    private final AdministratorMapper administratorMapper;

    public AdministratorServiceImpl(AdministratorMapper administratorMapper) {
        this.administratorMapper = administratorMapper;
    }

    @Override
    public AdministratorResponse createAdministrator(AdministratorCreateRequest request) {
        Administrator admin = new Administrator();
        BeanUtils.copyProperties(request, admin);
        admin.setAdminId(generateNewAdminId());

        // 在实际应用中，这里必须对密码进行加密
        // 例如：admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPassword(request.getPassword()); // 简化处理

        administratorMapper.insert(admin);
        return AdministratorResponse.fromEntity(admin);
    }

    @Override
    public AdministratorLoginResponse login(AdministratorLoginRequest request) {
        Administrator admin = administratorMapper.findByAccount(request.getAccount());
        if (admin == null) {
            throw new AuthenticationException("账号或密码错误");
        }

        // 在实际应用中，这里需要验证加密后的密码
        // if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
        if (!request.getPassword().equals(admin.getPassword())) { // 简化处理
            throw new AuthenticationException("账号或密码错误");
        }

        // 登录成功，生成并返回 Token
        // 这里的 Token 生成逻辑可以根据实际需要实现（例如使用 JWT）
        String token = "FAKE_JWT_TOKEN_FOR_" + admin.getAccount();
        return new AdministratorLoginResponse(admin.getAdminId(), admin.getName(), token);
    }


    @Override
    public PaginatedResponse<AdministratorResponse> getAdministrators(int page, int size) {
        PageHelper.startPage(page, size);
        List<Administrator> admins = administratorMapper.findAll();
        PageInfo<Administrator> pageInfo = new PageInfo<>(admins);

        List<AdministratorResponse> dtoList = pageInfo.getList().stream()
                .map(AdministratorResponse::fromEntity)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(pageInfo.getTotal(), dtoList);
    }

    @Override
    public AdministratorResponse getAdministratorById(String adminId) {
        Administrator admin = administratorMapper.findById(adminId);
        if (admin == null) {
            throw new ResourceNotFoundException("Administrator not found with id: " + adminId);
        }
        return AdministratorResponse.fromEntity(admin);
    }

    @Override
    public AdministratorResponse updateAdministrator(String adminId, AdministratorUpdateRequest request) {
        Administrator existingAdmin = administratorMapper.findById(adminId);
        if (existingAdmin == null) {
            throw new ResourceNotFoundException("Administrator not found with id: " + adminId);
        }

        Administrator adminToUpdate = new Administrator();
        BeanUtils.copyProperties(request, adminToUpdate);
        adminToUpdate.setAdminId(adminId);

        // 如果请求中包含密码，则进行加密处理
        if (StringUtils.hasText(request.getPassword())) {
            // adminToUpdate.setPassword(passwordEncoder.encode(request.getPassword()));
            adminToUpdate.setPassword(request.getPassword()); // 简化处理
        }

        administratorMapper.update(adminToUpdate);

        return AdministratorResponse.fromEntity(administratorMapper.findById(adminId));
    }

    @Override
    public void deleteAdministrator(String adminId) {
        if (administratorMapper.existsById(adminId) == 0) {
            throw new ResourceNotFoundException("Administrator not found with id: " + adminId);
        }
        administratorMapper.deleteById(adminId);
    }

    private synchronized String generateNewAdminId() {
        String maxId = administratorMapper.findMaxAdminId();
        if (!StringUtils.hasText(maxId)) {
            return "A001";
        }
        int number = Integer.parseInt(maxId.substring(1));
        return "A" + String.format("%03d", number + 1);
    }
}