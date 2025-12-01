package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.dto.ChargeRecordResponse;
import com.experiment.parkingsystem.entity.ChargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

@Mapper
public interface ChargeRecordMapper {

    int insert(ChargeRecord chargeRecord);

    // 该方法将执行JOIN连接查询，以获取响应所需的所有数据
    List<ChargeRecordResponse> findByCriteria(
            @Param("licensePlate") String licensePlate,
            @Param("vehicleId") String vehicleId,
            @Param("adminId") String adminId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime
    );

    String findMaxRecordId();
}