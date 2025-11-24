package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.MonthlyCard;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class MonthlyCardResponse {
    private String cardId;
    private String cardNo;
    private String vehicleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validityPeriod;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date renewTime;
    private String status;

    public static MonthlyCardResponse fromEntity(MonthlyCard card) {
        MonthlyCardResponse dto = new MonthlyCardResponse();
        dto.setCardId(card.getCardId());
        dto.setCardNo(card.getCardNo());
        dto.setVehicleId(card.getVehicleId());
        dto.setIssueTime(card.getIssueTime());
        dto.setValidityPeriod(card.getValidityPeriod());
        dto.setRenewTime(card.getRenewTime());
        dto.setStatus(card.getStatus());
        return dto;
    }
}