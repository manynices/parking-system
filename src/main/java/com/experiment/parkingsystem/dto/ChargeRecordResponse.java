package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.ChargeRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChargeRecordResponse {
    private String recordId;
    private String vehicleId;
    private String licensePlate; // 关联查询出的车牌号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date enterTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exitTime;

    private BigDecimal amount;
    private String paymentMethod;
    private String adminId;

    // 从实体类和车牌号创建响应DTO的辅助方法
    public static ChargeRecordResponse fromEntity(ChargeRecord record, String licensePlate) {
        ChargeRecordResponse dto = new ChargeRecordResponse();
        dto.setRecordId(record.getRecordId());
        dto.setVehicleId(record.getVehicleId());
        dto.setLicensePlate(licensePlate); // 设置车牌号
        dto.setEnterTime(record.getEnterTime());
        dto.setExitTime(record.getExitTime());
        dto.setAmount(record.getAmount());
        dto.setPaymentMethod(record.getPaymentMethod());
        dto.setAdminId(record.getAdminId());
        return dto;
    }
}