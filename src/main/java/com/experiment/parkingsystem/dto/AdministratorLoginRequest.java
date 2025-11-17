package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class AdministratorLoginRequest {
    private String account;
    private String password;
}