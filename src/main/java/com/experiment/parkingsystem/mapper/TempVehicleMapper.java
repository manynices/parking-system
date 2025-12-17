package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.TempVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TempVehicleMapper {
    int insert(TempVehicle vehicle);

    // 更新入场信息
    int updateEntry(TempVehicle vehicle);

    // 更新出场信息
    int updateExit(TempVehicle vehicle);

    // 查询列表
    List<TempVehicle> selectList(@Param("licensePlate") String licensePlate,
                                 @Param("status") String status,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate);

    // 根据车牌查询最近一条未完结的记录 (待入场 或 在场)
    TempVehicle selectLatestActiveByPlate(@Param("licensePlate") String licensePlate);
}