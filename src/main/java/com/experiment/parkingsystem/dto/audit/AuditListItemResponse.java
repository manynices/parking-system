package com.experiment.parkingsystem.dto.audit;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditListItemResponse {
    private String applicationId;   // "OA001" 或 "UVA001"
    private String type;            // "owner-verification" 或 "vehicle-bind"
    private String applicantName;
    private String licensePlate;    // 仅车辆绑定有
    private LocalDateTime applyTime;
    private String status;
}