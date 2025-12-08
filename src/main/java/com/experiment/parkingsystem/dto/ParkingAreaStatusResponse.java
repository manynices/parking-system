package com.experiment.parkingsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class ParkingAreaStatusResponse {
    private String parkingArea;
    private Long total;
    private Long free;
    private Long occupied;
    private List<ParkingSpaceResponse> spaceList;
}