package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TempVehicle {
    private Long registrationId;
    private String licensePlate;
    private String vehicleType;

    private String applicantName;
    private String applicantPhone;
    private String visitUnit;
    private String visitReason;
    private LocalDateTime expectedEnterTime;
    private LocalDateTime expectedExitTime;
    private String hostName;
    private String hostPhone;

    private LocalDateTime entryTime;
    private String entryDeviceId;
    private String entryImage;

    private LocalDateTime exitTime;
    private String exitDeviceId;
    private String exitImage;
    private Long parkingDuration;
    private BigDecimal parkingFee;
    private String paymentMethod;

    private String status;
    private String remark;
    private LocalDateTime createTime;
}