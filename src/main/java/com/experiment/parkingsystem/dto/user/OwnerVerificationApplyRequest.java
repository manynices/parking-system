package com.experiment.parkingsystem.dto.user;

import lombok.Data;
import java.util.List;

@Data
public class OwnerVerificationApplyRequest {
    private String name;
    private String idCard;
    private String address;
    private List<String> proofImages;
}