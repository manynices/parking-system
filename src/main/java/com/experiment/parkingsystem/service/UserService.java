package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.user.*;

public interface UserService {
    UserRegisterResponse register(UserRegisterRequest request);

    UserLoginResponse login(UserLoginRequest request);

    void forgotPassword(UserForgotPasswordRequest request);

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateUserProfile(UserProfileUpdateRequest request);

    void changePassword(UserChangePasswordRequest request);

    OwnerVerificationApplyResponse applyOwnerVerification(OwnerVerificationApplyRequest request);

    OwnerVerificationStatusResponse getOwnerVerificationStatus();

    PaginatedResponse<UserListItemResponse> listUsers(int page, int size, String account, String phone, Boolean isOwnerVerified, String name);
}