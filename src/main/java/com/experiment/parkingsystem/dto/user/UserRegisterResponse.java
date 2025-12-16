package com.experiment.parkingsystem.dto.user;

import lombok.Data;

@Data
public class UserRegisterResponse {
    private String userId;
    private String phone;
    private String account;
    private Boolean isOwnerVerified;
    private String status;
    private String token;
}