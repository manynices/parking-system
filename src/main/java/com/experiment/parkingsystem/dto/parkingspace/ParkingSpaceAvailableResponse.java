package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;
import java.util.List;

@Data
public class ParkingSpaceAvailableResponse {
    private String parkingArea;
    private Long total;
    private Long available;
    private List<ParkingSpaceItemResponse> spaceList;
}