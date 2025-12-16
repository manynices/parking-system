package com.experiment.parkingsystem.dto.user;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String account;
    private String password;
}