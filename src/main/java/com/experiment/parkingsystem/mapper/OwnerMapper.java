package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.Owner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OwnerMapper {

    int insert(Owner owner);

    Owner findById(@Param("ownerId") String ownerId);

    List<Owner> findAll(@Param("name") String name);

    int update(Owner owner);

    int deleteById(@Param("ownerId") String ownerId);

    int existsById(@Param("ownerId") String ownerId);

    String findMaxOwnerId();
}