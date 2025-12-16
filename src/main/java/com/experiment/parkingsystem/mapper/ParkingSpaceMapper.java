package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.ParkingSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ParkingSpaceMapper {
    int updateStatus(ParkingSpace space);

    int updatePurchaseInfo(ParkingSpace space);

    ParkingSpace selectBySpaceNo(@Param("spaceNo") String spaceNo);

    // 通用列表查询 (关联 vehicle 和 sys_user 表)
    List<ParkingSpace> selectList(@Param("parkingArea") String parkingArea,
                                  @Param("status") String status,
                                  @Param("spaceNo") String spaceNo,
                                  @Param("spaceType") String spaceType,
                                  @Param("ownerId") Long ownerId);

    // 统计数量
    long countByAreaAndStatus(@Param("parkingArea") String parkingArea,
                              @Param("status") String status,
                              @Param("spaceType") String spaceType);
}