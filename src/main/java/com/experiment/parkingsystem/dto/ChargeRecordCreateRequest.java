package com.experiment.parkingsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeRecordCreateRequest {
    private String vehicleId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enterTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exitTime;

    private BigDecimal amount;

    private String paymentMethod;

    private String adminId;
}