package com.experiment.parkingsystem.dto.device;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceResponse {
    private String deviceId; // "D001"
    private String deviceType;
    private String deviceNo;
    private String installationLocation;
    private String parkingArea;
    private String status;
    private String faultRemark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}