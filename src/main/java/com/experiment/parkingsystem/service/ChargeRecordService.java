package com.experiment.parkingsystem.service;

import com.experiment.parkingsystem.common.PaginatedResponse;
import com.experiment.parkingsystem.dto.ChargeRecordCreateRequest;
import com.experiment.parkingsystem.dto.ChargeRecordResponse;
import java.util.Date;

public interface ChargeRecordService {
    ChargeRecordResponse createChargeRecord(ChargeRecordCreateRequest request);

    PaginatedResponse<ChargeRecordResponse> getChargeRecords(
            int page, int size,
            String licensePlate, String vehicleId, String adminId,
            Date startTime, Date endTime
    );
}