package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DeviceMapper {
    int insert(Device device);

    Device findById(@Param("deviceId") String deviceId);

    List<Device> findByCriteria(
            @Param("deviceType") String deviceType,
            @Param("status") String status,
            @Param("parkingArea") String parkingArea
    );

    int update(Device device);

    String findMaxDeviceId();
}