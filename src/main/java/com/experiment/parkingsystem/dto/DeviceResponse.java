package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.Device;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class DeviceResponse {
    private String deviceId;
    private String deviceType;
    private String deviceNo;
    private String installationLocation;
    private String parkingArea;
    private String status;
    private String faultRemark; // 在响应中也包含备注

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public static DeviceResponse fromEntity(Device device) {
        DeviceResponse dto = new DeviceResponse();
        dto.setDeviceId(device.getDeviceId());
        dto.setDeviceType(device.getDeviceType());
        dto.setDeviceNo(device.getDeviceNo());
        dto.setInstallationLocation(device.getInstallationLocation());
        dto.setParkingArea(device.getParkingArea());
        dto.setStatus(device.getStatus());
        dto.setFaultRemark(device.getFaultRemark());
        dto.setCreateTime(device.getCreateTime());
        dto.setUpdateTime(device.getUpdateTime());
        return dto;
    }
}