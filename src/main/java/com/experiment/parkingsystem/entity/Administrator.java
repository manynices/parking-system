package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Administrator {
    private Long adminId;
    private String account;
    private String password;
    private String name;
    private String permissionLevel;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}