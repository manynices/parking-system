package com.experiment.parkingsystem.dto.parkingspace;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ParkingSpaceSalesResponse {
    private Long total;
    private Long available;
    private BigDecimal price;
    private List<ParkingSpaceItemResponse> spaceList;
}