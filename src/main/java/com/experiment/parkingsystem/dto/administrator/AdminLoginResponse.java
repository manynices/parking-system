package com.experiment.parkingsystem.dto.administrator;

import lombok.Data;

@Data
public class AdminLoginResponse {
    private String adminId;
    private String name;
    private String account;
    private String permissionLevel;
    private String token;
}