package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.Administrator;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // 密码等敏感字段不返回
public class AdministratorResponse {
    private String adminId;
    private String name;
    private String account;
    private String permissionLevel;

    public static AdministratorResponse fromEntity(Administrator admin) {
        AdministratorResponse dto = new AdministratorResponse();
        dto.setAdminId(admin.getAdminId());
        dto.setName(admin.getName());
        dto.setAccount(admin.getAccount());
        dto.setPermissionLevel(admin.getPermissionLevel());
        return dto;
    }
}