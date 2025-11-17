package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class AdministratorUpdateRequest {
    private String name;
    private String password; // 允许更新密码，但需要加密处理
    private String permissionLevel;
}