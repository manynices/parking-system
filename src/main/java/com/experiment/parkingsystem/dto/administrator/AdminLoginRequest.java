package com.experiment.parkingsystem.dto.administrator;

import lombok.Data;

@Data
public class AdminLoginRequest {
    private String account;
    private String password;
}