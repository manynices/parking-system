package com.experiment.parkingsystem.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OwnerVerification {
    private Long applicationId;
    private Long userId;
    private String name;
    private String idCard;
    private String address;
    private String proofImages; // JSON字符串格式存储图片URL列表
    private String status;      // "待审核", "通过", "拒绝"
    private String verifierName;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime verifiedTime;
}