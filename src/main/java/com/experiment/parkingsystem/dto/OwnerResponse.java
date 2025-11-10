package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.Owner;
import lombok.Data;

@Data
public class OwnerResponse {
    private String ownerId;
    private String name;
    private String contact;
    private String address;
    private String idCard;

    public static OwnerResponse fromEntity(Owner owner) {
        OwnerResponse dto = new OwnerResponse();
        dto.setOwnerId(owner.getOwnerId());
        dto.setName(owner.getName());
        dto.setContact(owner.getContact());
        dto.setAddress(owner.getAddress());
        dto.setIdCard(owner.getIdCard());
        return dto;
    }
}