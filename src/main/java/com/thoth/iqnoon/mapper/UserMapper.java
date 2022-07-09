package com.thoth.iqnoon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thoth.iqnoon.entities.UserEntity;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<UserEntity> {

    Integer queryCount();

    UserEntity queryUserByUserId(@Param("userId") String userId);

}
