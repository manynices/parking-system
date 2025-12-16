package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VehicleMapper {
    int insert(Vehicle vehicle);
    int updateById(Vehicle vehicle);
    int deleteById(@Param("vehicleId") Long vehicleId);

    Vehicle selectById(@Param("vehicleId") Long vehicleId);
    Vehicle selectByLicensePlate(@Param("licensePlate") String licensePlate);

    // 列表查询，支持关联用户表获取姓名
    List<Vehicle> selectList(@Param("ownerId") Long ownerId,
                             @Param("userId") Long userId,
                             @Param("status") String status,
                             @Param("vehicleClass") String vehicleClass,
                             @Param("licensePlate") String licensePlate);

    // 简单查询，不关联
    List<Vehicle> selectByUserId(@Param("userId") Long userId);
}