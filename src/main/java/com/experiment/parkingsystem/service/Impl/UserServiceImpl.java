package com.experiment.parkingsystem.service.impl;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.common.UserContext;
import com.experiment.parkingsystem.dto.user.*;
import com.experiment.parkingsystem.entity.OwnerVerification;
import com.experiment.parkingsystem.entity.User;
import com.experiment.parkingsystem.mapper.OwnerVerificationMapper;
import com.experiment.parkingsystem.mapper.UserMapper;
import com.experiment.parkingsystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final OwnerVerificationMapper verificationMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserServiceImpl(UserMapper userMapper, OwnerVerificationMapper verificationMapper) {
        this.userMapper = userMapper;
        this.verificationMapper = verificationMapper;
    }

    private Long getCurrentUserId() {
        // 从 ThreadLocal 获取拦截器解析出来的 ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            // 如果拦截器逻辑正确，理论上不会走到这里，除非是开放接口调用了此方法
            throw new RuntimeException("未登录用户无法执行此操作");
        }
        return userId;
    }

    private String formatUserId(Long id) {
        return "U" + String.format("%03d", id);
    }

    private String formatAppId(Long id) {
        return "OA" + String.format("%03d", id);
    }

    @Override
    @Transactional
    public UserRegisterResponse register(UserRegisterRequest request) {
        if (userMapper.selectByAccount(request.getAccount()) != null) {
            throw new RuntimeException("账号已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setStatus("正常");
        user.setIsOwnerVerified(false);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        UserRegisterResponse response = new UserRegisterResponse();
        BeanUtils.copyProperties(user, response);
        response.setUserId(formatUserId(user.getUserId()));
        response.setToken("USER_JWT_TOKEN_" + user.getUserId());
        return response;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User user = userMapper.selectByAccount(request.getAccount());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        UserLoginResponse response = new UserLoginResponse();
        BeanUtils.copyProperties(user, response);
        response.setUserId(formatUserId(user.getUserId()));
        response.setToken("USER_JWT_TOKEN_" + user.getUserId());
        return response;
    }

    @Override
    public void forgotPassword(UserForgotPasswordRequest request) {
        User user = userMapper.selectByAccount(request.getAccount());
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        userMapper.updatePassword(request.getAccount(), request.getNewPassword());
    }

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User user = userMapper.selectById(getCurrentUserId());
        if (user == null) throw new RuntimeException("用户不存在");
        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);
        response.setUserId(formatUserId(user.getUserId()));
        return response;
    }

    @Override
    public UserProfileResponse updateUserProfile(UserProfileUpdateRequest request) {
        User user = userMapper.selectById(getCurrentUserId());
        BeanUtils.copyProperties(request, user);
        userMapper.updateById(user);
        return getCurrentUserProfile();
    }

    @Override
    public void changePassword(UserChangePasswordRequest request) {
        User user = userMapper.selectById(getCurrentUserId());
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        user.setPassword(request.getNewPassword());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public OwnerVerificationApplyResponse applyOwnerVerification(OwnerVerificationApplyRequest request) {
        Long userId = getCurrentUserId();
        OwnerVerification verification = new OwnerVerification();
        BeanUtils.copyProperties(request, verification);
        verification.setUserId(userId);
        verification.setStatus("待审核");
        verification.setCreateTime(LocalDateTime.now());
        try {
            verification.setProofImages(objectMapper.writeValueAsString(request.getProofImages()));
        } catch (Exception e) {
            verification.setProofImages("[]");
        }
        verificationMapper.insert(verification);

        OwnerVerificationApplyResponse response = new OwnerVerificationApplyResponse();
        BeanUtils.copyProperties(verification, response);
        response.setApplicationId(formatAppId(verification.getApplicationId()));
        response.setUserId(formatUserId(userId));
        return response;
    }

    @Override
    public OwnerVerificationStatusResponse getOwnerVerificationStatus() {
        Long userId = getCurrentUserId();
        User user = userMapper.selectById(userId);
        OwnerVerification latest = verificationMapper.selectLatestByUserId(userId);

        OwnerVerificationStatusResponse response = new OwnerVerificationStatusResponse();
        response.setIsOwnerVerified(user.getIsOwnerVerified());
        if (latest != null) {
            response.setVerifiedTime(latest.getVerifiedTime());
            response.setVerifierName(latest.getVerifierName());
            response.setRemark(latest.getRemark());
        }
        return response;
    }

    // --- 【修改点】列表查询方法 ---
    @Override
    public PaginatedResponse<UserListItemResponse> listUsers(int page, int size, String account, String phone, Boolean isOwnerVerified, String name) {
        PageHelper.startPage(page, size);
        List<User> userList = userMapper.selectList(account, phone, isOwnerVerified, name);
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        List<UserListItemResponse> dtoList = userList.stream().map(user -> {
            UserListItemResponse dto = new UserListItemResponse();
            BeanUtils.copyProperties(user, dto);
            dto.setUserId(formatUserId(user.getUserId()));
            return dto;
        }).collect(Collectors.toList());

        // 【修改这里】
        // 你的 PaginatedResponse 只有 (long total, List<T> list) 构造函数
        // 必须显式指定泛型 <UserListItemResponse> 避免类型推断错误
        return new PaginatedResponse<UserListItemResponse>(pageInfo.getTotal(), dtoList);
    }
}