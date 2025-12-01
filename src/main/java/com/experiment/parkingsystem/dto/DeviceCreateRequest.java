package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class DeviceCreateRequest {
    private String deviceType;
    private String deviceNo;
    private String installationLocation;
    private String parkingArea;
    private String status;
}