package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.MonthlyCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MonthlyCardMapper {
    int insert(MonthlyCard card);

    int updateById(MonthlyCard card);

    MonthlyCard selectById(@Param("cardId") Long cardId);

    // 列表查询，关联车辆和用户表
    List<MonthlyCard> selectList(@Param("vehicleId") Long vehicleId,
                                 @Param("status") String status,
                                 @Param("ownerId") Long ownerId,
                                 @Param("userId") Long userId);
}