package com.experiment.parkingsystem.mapper;

import com.experiment.parkingsystem.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    int insert(User user);

    User selectByAccount(@Param("account") String account);

    User selectById(@Param("userId") Long userId);

    int updateById(User user);

    int updatePassword(@Param("account") String account, @Param("password") String password);

    // PageHelper 会自动处理分页，这里不需要传入 offset 和 limit
    List<User> selectList(@Param("account") String account,
                          @Param("phone") String phone,
                          @Param("isOwnerVerified") Boolean isOwnerVerified,
                          @Param("name") String name);
}