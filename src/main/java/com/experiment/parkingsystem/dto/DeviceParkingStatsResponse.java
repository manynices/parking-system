package com.experiment.parkingsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceParkingStatsResponse {
    private String deviceId;
    private String parkingArea;
    private Integer totalSpaces;
    private Integer usedSpaces;
    private Integer freeSpaces;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}