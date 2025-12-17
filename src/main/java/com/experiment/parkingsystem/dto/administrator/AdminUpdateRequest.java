package com.experiment.parkingsystem.dto.administrator;

import lombok.Data;

@Data
public class AdminUpdateRequest {
    private String name;
    private String password; // 可选，为空则不修改
    private String permissionLevel;
}