package com.experiment.parkingsystem.dto;

import lombok.Data;

@Data
public class OwnerCreateRequest {
    private String name;
    private String contact;
    private String address;
    private String idCard;
}