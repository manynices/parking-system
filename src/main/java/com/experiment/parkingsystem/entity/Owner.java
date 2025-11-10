package com.experiment.parkingsystem.entity;

import lombok.Data;

@Data
public class Owner {
    private String ownerId;
    private String name;
    private String contact;
    private String address;
    private String idCard;
}