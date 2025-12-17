package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.administrator.*;

public interface AdministratorService {
    AdminResponse createAdmin(AdminCreateRequest request);

    AdminLoginResponse login(AdminLoginRequest request);

    PaginatedResponse<AdminResponse> listAdmins(int page, int size, String name);

    AdminResponse getAdminById(String adminIdStr);

    AdminResponse updateAdmin(String adminIdStr, AdminUpdateRequest request);

    void deleteAdmin(String adminIdStr);
}