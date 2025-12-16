package com.experiment.parkingsystem.dto.user;

import lombok.Data;

@Data
public class UserLoginResponse {
    private String userId;
    private String phone;
    private String account;
    private String name;
    private Boolean isOwnerVerified;
    private String token;
}