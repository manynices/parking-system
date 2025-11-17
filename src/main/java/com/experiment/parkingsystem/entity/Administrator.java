package com.experiment.parkingsystem.entity;

import lombok.Data;

@Data
public class Administrator {
    private String adminId; // 管理员唯一标识
    private String name; // 姓名
    private String account; // 登录账号
    private String password; // 密码
    private String permissionLevel; // 权限级别
}