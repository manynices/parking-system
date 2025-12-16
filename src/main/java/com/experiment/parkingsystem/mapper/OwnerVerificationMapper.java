package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.OwnerVerification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OwnerVerificationMapper {
    int insert(OwnerVerification verification);

    OwnerVerification selectLatestByUserId(@Param("userId") Long userId);
}