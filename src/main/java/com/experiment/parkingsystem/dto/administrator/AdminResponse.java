package com.experiment.parkingsystem.dto.administrator;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminResponse {
    private String adminId;
    private String name;
    private String account;
    // 不返回密码
    private String permissionLevel;
    private LocalDateTime createTime;
}