package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.*;

public interface MonthlyCardService {
    MonthlyCardResponse createMonthlyCard(MonthlyCardCreateRequest request);
    MonthlyCardResponse renewMonthlyCard(String cardId, MonthlyCardRenewRequest request);
    MonthlyCardResponse updateMonthlyCardStatus(String cardId, MonthlyCardStatusRequest request);
    PaginatedResponse<MonthlyCardResponse> getMonthlyCards(int page, int size, String vehicleId, String status);
}