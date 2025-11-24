package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class MonthlyCardStatusRequest {
    /**
     * 要更新的状态，例如 "挂失" 或 "正常"
     */
    private String status;

    /**
     * 操作的管理员ID
     */
    private String adminId;
}