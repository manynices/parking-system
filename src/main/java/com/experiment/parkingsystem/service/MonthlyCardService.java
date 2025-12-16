package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.monthlycard.*;
import java.util.List;

public interface MonthlyCardService {
    MonthlyCardResponse createCard(MonthlyCardRequest request);

    MonthlyCardResponse renewCard(String cardIdStr, MonthlyCardRenewRequest request);

    MonthlyCardResponse updateStatus(String cardIdStr, MonthlyCardStatusRequest request);

    PaginatedResponse<MonthlyCardResponse> listCards(int page, int size, String vehicleIdStr, String status, String ownerIdStr, String userIdStr);

    MonthlyCardResponse getCardById(String cardIdStr);

    List<MonthlyCardPackageResponse> getPackages();
}