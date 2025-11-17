package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class AdministratorCreateRequest {
    private String name;
    private String account;
    private String password;
    private String permissionLevel;
}