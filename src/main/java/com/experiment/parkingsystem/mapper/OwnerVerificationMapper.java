package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.OwnerVerification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OwnerVerificationMapper {
    int insert(OwnerVerification verification);

    // 【新增】更新审核状态
    int updateStatus(OwnerVerification verification);

    OwnerVerification selectLatestByUserId(@Param("userId") Long userId);

    // 【新增】根据ID查询
    OwnerVerification selectById(@Param("applicationId") Long applicationId);

    // 【新增】查询待审核列表
    List<OwnerVerification> selectPendingList();
}