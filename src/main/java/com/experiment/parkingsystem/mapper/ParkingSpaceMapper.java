package com.experiment.parkingsystem.mapper;
import com.experiment.parkingsystem.entity.ParkingSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface ParkingSpaceMapper {
    ParkingSpace findBySpaceNo(@Param("spaceNo") String spaceNo);
    int update(ParkingSpace parkingSpace);
    List<ParkingSpace> findByCriteria(@Param("parkingArea") String parkingArea, @Param("status") String status);
    long countByAreaAndStatus(@Param("parkingArea") String parkingArea, @Param("status") String status);
}