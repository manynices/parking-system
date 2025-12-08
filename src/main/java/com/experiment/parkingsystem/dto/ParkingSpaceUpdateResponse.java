package com.experiment.parkingsystem.dto;

import com.experiment.parkingsystem.entity.ParkingSpace;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * 用于 7.2 更新车位状态接口的响应体
 */
@Data
public class ParkingSpaceUpdateResponse {

    /**
     * 车位编号
     */
    private String spaceNo;

    /**
     * 所属车库区域
     */
    private String parkingArea;

    /**
     * 车位状态 (空闲/占用)
     */
    private String status;

    /**
     * 占用车辆的ID
     */
    private String vehicleId;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 从实体类 (Entity) 转换到本 DTO 的静态方法
     * @param space 数据库查询出的 ParkingSpace 实体对象
     * @return 转换后的响应 DTO 对象
     */
    public static ParkingSpaceUpdateResponse fromEntity(ParkingSpace space) {
        ParkingSpaceUpdateResponse dto = new ParkingSpaceUpdateResponse();
        dto.setSpaceNo(space.getSpaceNo());
        dto.setParkingArea(space.getParkingArea());
        dto.setStatus(space.getStatus());
        dto.setVehicleId(space.getVehicleId());
        dto.setUpdateTime(space.getUpdateTime());
        return dto;
    }
}