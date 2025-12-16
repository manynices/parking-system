package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VehicleMapper {

    int insert(Vehicle vehicle);

    Vehicle findById(@Param("vehicleId") String vehicleId);

    // 查询列表，根据 ownerId 和 status 进行过滤
    List<Vehicle> findByCriteria(@Param("ownerId") String ownerId, @Param("status") String status, @Param("licensePlate") String licensePlate);

    int update(Vehicle vehicle);

    int deleteById(@Param("vehicleId") String vehicleId);

    int existsById(@Param("vehicleId") String vehicleId);

    // 用于生成新的 vehicleId
    String findMaxVehicleId();
}