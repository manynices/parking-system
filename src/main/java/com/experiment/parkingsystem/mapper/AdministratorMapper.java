package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdministratorMapper {
    int insert(Administrator admin);

    int update(Administrator admin);

    int deleteById(@Param("adminId") Long adminId);

    Administrator selectById(@Param("adminId") Long adminId);

    Administrator selectByAccount(@Param("account") String account);

    List<Administrator> selectList(@Param("name") String name);
}