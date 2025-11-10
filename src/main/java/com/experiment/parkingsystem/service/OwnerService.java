package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.OwnerCreateRequest;
import com.experiment.parkingsystem.dto.OwnerResponse;
import com.experiment.parkingsystem.dto.OwnerUpdateRequest;

public interface OwnerService {
    OwnerResponse createOwner(OwnerCreateRequest request);
    PaginatedResponse<OwnerResponse> getOwners(int page, int size, String name);
    OwnerResponse getOwnerById(String ownerId);
    OwnerResponse updateOwner(String ownerId, OwnerUpdateRequest request);
    void deleteOwner(String ownerId);
}