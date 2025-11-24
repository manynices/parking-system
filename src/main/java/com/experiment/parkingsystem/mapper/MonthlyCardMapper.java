package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.MonthlyCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MonthlyCardMapper {

    int insert(MonthlyCard monthlyCard);

    MonthlyCard findById(@Param("cardNo") String cardNo);

    // 查询列表，根据 vehicleId 和 status 进行过滤
    List<MonthlyCard> findByCriteria(@Param("vehicleId") String vehicleId, @Param("status") String status);

    int update(MonthlyCard monthlyCard);

    int existsById(@Param("cardId") String cardId);

    // 用于生成新的 cardId
    String findMaxCardId();
}