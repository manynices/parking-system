package com.experiment.parkingsystem.dto.user;

import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private String name;
    private String idCard;
    private String email;
    private String avatar;
}