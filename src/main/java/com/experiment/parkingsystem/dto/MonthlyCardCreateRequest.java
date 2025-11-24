package com.experiment.parkingsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class MonthlyCardCreateRequest {
    private String cardNo;
    private String vehicleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validityPeriod;
    private String adminId;
}