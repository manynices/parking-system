package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;
import java.util.List;

@Data
public class ParkingSpaceStatsResponse {
    private String parkingArea;
    private Long total;
    private Long free;
    private Long occupied;
    private List<ParkingSpaceItemResponse> spaceList;
}