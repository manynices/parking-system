package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.util.Date;

@Data
public class MonthlyCard {
    private String cardId;
    private String cardNo;
    private String vehicleId;
    private Date issueTime;
    private Date validityPeriod;
    private Date renewTime;
    private String status;
    private String adminId;
}