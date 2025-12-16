package com.experiment.parkingsystem.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OwnerVerificationStatusResponse {
    private Boolean isOwnerVerified;
    private LocalDateTime verifiedTime;
    private String verifierName;
    private String remark;
}