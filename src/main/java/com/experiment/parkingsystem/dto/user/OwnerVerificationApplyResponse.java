package com.experiment.parkingsystem.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OwnerVerificationApplyResponse {
    private String applicationId;
    private String userId;
    private String name;
    private String status;
    private LocalDateTime createTime;
}