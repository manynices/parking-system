package com.experiment.parkingsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorLoginResponse {
    private String adminId;
    private String name;
    private String token; // 登录凭证
}