package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long userId;        // 数据库主键
    private String phone;
    private String account;
    private String password;    // 实际项目中应存储加密后的密码
    private String name;
    private String idCard;
    private String email;
    private String avatar;
    private Boolean isOwnerVerified;
    private String status;      // "正常", "禁用"
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}