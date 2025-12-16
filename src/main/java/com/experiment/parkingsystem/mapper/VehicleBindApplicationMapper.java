package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.VehicleBindApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VehicleBindApplicationMapper {
    int insert(VehicleBindApplication application);
}