package com.experiment.parkingsystem.dto.vehicle;

import lombok.Data;
import java.util.List;

@Data
public class VehicleBindRequest {
    private String licensePlate;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleColor;
    private List<String> proofImages;
    private String remark;
}