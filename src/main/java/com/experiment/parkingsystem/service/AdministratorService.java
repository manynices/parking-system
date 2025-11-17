package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;

public interface AdministratorService {
    AdministratorResponse createAdministrator(AdministratorCreateRequest request);
    AdministratorLoginResponse login(AdministratorLoginRequest request);
    PaginatedResponse<AdministratorResponse> getAdministrators(int page, int size);
    AdministratorResponse getAdministratorById(String adminId);
    AdministratorResponse updateAdministrator(String adminId, AdministratorUpdateRequest request);
    void deleteAdministrator(String adminId);
}