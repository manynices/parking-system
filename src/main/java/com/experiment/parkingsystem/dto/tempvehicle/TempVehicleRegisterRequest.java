package com.experiment.parkingsystem.dto.tempvehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TempVehicleRegisterRequest {
    private String licensePlate;
    private String vehicleType;
    private String applicantName;
    private String applicantPhone;
    private String visitUnit;
    private String visitReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedEnterTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedExitTime;

    private String hostName;
    private String hostPhone;
}