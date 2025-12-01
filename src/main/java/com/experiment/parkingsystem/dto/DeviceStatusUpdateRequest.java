package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class DeviceStatusUpdateRequest {
    private String status; // "正常" 或 "故障"
    private String faultRemark; // 故障备注
    private String adminId; // 操作的管理员ID (用于记录日志等，此处不直接存入device表)
}