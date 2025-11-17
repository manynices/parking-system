package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Administrator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdministratorMapper {

    int insert(Administrator administrator);

    Administrator findById(@Param("adminId") String adminId);

    Administrator findByAccount(@Param("account") String account);

    List<Administrator> findAll();

    int update(Administrator administrator);

    int deleteById(@Param("adminId") String adminId);

    int existsById(@Param("adminId") String adminId);

    // 用于生成新的 adminId
    String findMaxAdminId();
}