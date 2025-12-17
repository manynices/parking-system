package com.experiment.parkingsystem.dto.audit;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditReviewResponse {
    private String applicationId;
    private String status;
    private LocalDateTime reviewTime;
    private String remark;
}