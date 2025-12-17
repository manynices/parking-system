package com.experiment.parkingsystem.dto.device;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeviceParkingStatsResponse {
    private String deviceId;
    private String parkingArea;
    private Long totalSpaces;
    private Long usedSpaces;
    private Long freeSpaces;
    private LocalDateTime updateTime;
}