package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.ChargeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChargeRecordMapper {
    int insert(ChargeRecord record);

    List<ChargeRecord> selectList(@Param("licensePlate") String licensePlate,
                                  @Param("vehicleId") Long vehicleId,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime,
                                  @Param("ownerId") Long ownerId,
                                  @Param("userId") Long userId);
}