package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.VehicleBindApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface VehicleBindApplicationMapper {
    int insert(VehicleBindApplication application);

    // 【新增】
    int updateStatus(VehicleBindApplication application);

    VehicleBindApplication selectById(@Param("applicationId") Long applicationId);

    List<VehicleBindApplication> selectPendingList();
}