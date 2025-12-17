package com.experiment.parkingsystem.dto.administrator;

import lombok.Data;

@Data
public class AdminCreateRequest {
    private String name;
    private String account;
    private String password;
    private String permissionLevel; // "普通" / "高级"
}