package com.experiment.parkingsystem.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserListItemResponse {
    private String userId;
    private String phone;
    private String account;
    private String name;
    private Boolean isOwnerVerified;
    private String status;
    private LocalDateTime createTime;
}