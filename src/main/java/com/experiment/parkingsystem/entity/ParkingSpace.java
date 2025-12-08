package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ParkingSpace {
    private String spaceNo;
    private String parkingArea;
    private String status;
    private String vehicleId;
    private Date updateTime;
}