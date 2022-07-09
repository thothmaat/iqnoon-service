package com.thoth.iqnoon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thoth.iqnoon.entities.UserEntity;
import com.thoth.iqnoon.mapper.UserMapper;
import com.thoth.iqnoon.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
}
