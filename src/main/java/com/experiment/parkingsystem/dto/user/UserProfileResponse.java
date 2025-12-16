package com.experiment.parkingsystem.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserProfileResponse {
    private String userId;
    private String phone;
    private String account;
    private String name;
    private Boolean isOwnerVerified;
    private String idCard;
    private String email;
    private String avatar;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}