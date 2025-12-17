package com.experiment.parkingsystem.dto.tempvehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TempVehicleEntryRequest {
    private String licensePlate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    private String deviceId;
    private String imageUrl;
    private String remark;
}