package com.experiment.parkingsystem.dto.monthlycard;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyCardPackageResponse {
    private String packageType;
    private Integer durationMonths;
    private BigDecimal price;
    private String description;
}