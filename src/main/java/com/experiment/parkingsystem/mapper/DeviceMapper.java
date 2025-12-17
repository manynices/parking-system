package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DeviceMapper {
    int insert(Device device);

    int updateStatus(Device device);

    Device selectById(@Param("deviceId") Long deviceId);

    List<Device> selectList(@Param("deviceType") String deviceType,
                            @Param("status") String status,
                            @Param("parkingArea") String parkingArea);
}