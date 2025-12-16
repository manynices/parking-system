package com.experiment.parkingsystem.dto.monthlycard;

import lombok.Data;

@Data
public class MonthlyCardRenewRequest {
    private String packageType;
    private String paymentMethod;
}