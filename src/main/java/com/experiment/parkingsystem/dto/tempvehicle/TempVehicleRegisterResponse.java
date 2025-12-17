package com.experiment.parkingsystem.dto.tempvehicle;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TempVehicleRegisterResponse {
    private String registrationId; // "TR001"
    private String licensePlate;
    private String applicantName;
    private String applicantPhone;
    private String visitUnit;
    private LocalDateTime expectedEnterTime;
    private LocalDateTime expectedExitTime;
    private String status;
    private LocalDateTime createTime;
}