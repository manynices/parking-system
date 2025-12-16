package com.experiment.parkingsystem.dto.user;

import lombok.Data;

@Data
public class UserForgotPasswordRequest {
    private String account;
    private String newPassword;
}