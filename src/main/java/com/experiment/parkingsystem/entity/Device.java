package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Device {
    private String deviceId;
    private String deviceType;
    private String deviceNo;
    private String installationLocation;
    private String parkingArea;
    private String status;
    private String faultRemark;
    private Date createTime;
    private Date updateTime;
}