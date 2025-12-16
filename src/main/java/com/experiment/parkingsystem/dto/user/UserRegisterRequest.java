package com.experiment.parkingsystem.dto.user;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String phone;
    private String account;
    private String password;
}