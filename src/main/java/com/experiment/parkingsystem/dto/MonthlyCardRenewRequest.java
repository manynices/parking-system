package com.experiment.parkingsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class MonthlyCardRenewRequest {
    /**
     * 新的有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date newValidityPeriod;

    /**
     * 操作的管理员ID
     */
    private String adminId;
}