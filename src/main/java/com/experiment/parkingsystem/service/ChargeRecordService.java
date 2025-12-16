package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.chargerecord.*;

public interface ChargeRecordService {
    ChargeRecordCreateResponse createRecord(ChargeRecordCreateRequest request);

    PaginatedResponse<ChargeRecordListItemResponse> listRecords(
            int page, int size,
            String licensePlate, String vehicleIdStr,
            String startTime, String endTime,
            String ownerIdStr, String userIdStr
    );

    FeeCalculateResponse calculateFee(FeeCalculateRequest request);
}