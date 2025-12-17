package com.experiment.parkingsystem.dto.device;

import lombok.Data;

@Data
public class DeviceStatusUpdateRequest {
    private String status;
    private String faultRemark;
}