package com.thoth.iqnoon.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thoth.iqnoon.common.PageQuery;
import com.thoth.iqnoon.common.PageResult;
import com.thoth.iqnoon.common.ReqBody;
import com.thoth.iqnoon.common.RespBody;
import com.thoth.iqnoon.entities.UserEntity;
import com.thoth.iqnoon.request.CreateUserParam;
import com.thoth.iqnoon.request.QueryUserParam;
import com.thoth.iqnoon.response.UserDto;
import com.thoth.iqnoon.service.UserService;
import com.thoth.iqnoon.utils.SnowflakeWorker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@ApiModel("用户相关接口")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "根据用户id获取用户信息",notes = "根据用户id获取用户信息")
    @RequestMapping(value = "/queryByUserId",method = RequestMethod.POST)
    public RespBody<UserDto> queryByUserId(@RequestBody ReqBody<String> reqBody){

        String param = reqBody.getParam();
        UserEntity userEntity = userService.getById(param);
        UserDto userDto = UserDto.builder()
                .age(userEntity.getAge())
                .createBy(userEntity.getCreateBy())
                .userId(userEntity.getUserId())
                .createTime(userEntity.getCreateTime())
                .email(userEntity.getEmail())
                .gender(userEntity.getGender())
                .identityCardNumber(userEntity.getIdentityCardNumber())
                .telephone(userEntity.getTelephone())
                .updateBy(userEntity.getUpdateBy())
                .userName(userEntity.getUserName())
                .userStatus(userEntity.getUserStatus())
                .updateTime(userEntity.getUpdateTime())
                .build();
        return new RespBody<>(userDto);
    }

    @PostMapping("/create")
    @ApiOperation(value = "新增用户")
    public RespBody<Boolean> createUser(@RequestBody @Valid ReqBody<CreateUserParam> reqBody){
        CreateUserParam user = reqBody.getParam();
        UserEntity userEntity = UserEntity.builder()
                .age(user.getAge())
                .createBy(user.getCreateBy())
                .createTime(LocalDateTime.now())
                .email(user.getEmail())
                .gender(user.getGender())
                .identityCardNumber(user.getIdentityCardNumber())
                .telephone(user.getTelephone())
                .userId(SnowflakeWorker.generateId().toString())
                .userName(user.getUserName())
                .userStatus(user.getUserStatus())
                .build();

        boolean success = userService.save(userEntity);

        return new RespBody<>(success);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询用户列表信息")
    public RespBody<PageResult<UserDto>> pageUserList(@RequestBody @Valid ReqBody<PageQuery<QueryUserParam>> reqBody){

        PageQuery<QueryUserParam> pageQuery = reqBody.getParam();

        QueryUserParam param = pageQuery.getParam();

        String userName = param.getUserName();

        Page<UserEntity> page = new Page<>(pageQuery.getPageNum(),pageQuery.getPageSize());
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<UserEntity>()
                .eq(ObjectUtils.isNotEmpty(param.getUserStatus()), UserEntity::getUserStatus, param.getUserStatus())
                .like(ObjectUtils.isNotEmpty(param.getUserName()),UserEntity::getUserName,param.getUserName())
                .eq(ObjectUtils.isNotEmpty(param.getGender()),UserEntity::getGender,param.getGender())
                .eq(ObjectUtils.isNotEmpty(param.getIdentityCardNumber()),UserEntity::getIdentityCardNumber,param.getIdentityCardNumber())
                .eq(ObjectUtils.isNotEmpty(param.getTelephone()),UserEntity::getTelephone,param.getTelephone());

        Page<UserEntity> pageEntities = userService.page(page, lambdaQueryWrapper);

        List<UserEntity> records = pageEntities.getRecords();

        PageResult<UserDto> pageResult = new PageResult<>();
        pageResult.setPageNum(pageEntities.getCurrent());
        pageResult.setPageSize(pageEntities.getSize());
        pageResult.setTotalPage(pageEntities.getPages());
        pageResult.setTotal(pageEntities.getTotal());
        pageResult.setRows(records.stream().map(this::buildUserDto).collect(Collectors.toList()));
        return new RespBody<>(pageResult);
    }

    private UserDto buildUserDto(UserEntity entity){

        return UserDto.builder()
                .userId(entity.getUserId())
                .userStatus(entity.getUserStatus())
                .updateTime(entity.getUpdateTime())
                .updateBy(entity.getUpdateBy())
                .userName(entity.getUserName())
                .telephone(entity.getTelephone())
                .identityCardNumber(entity.getIdentityCardNumber())
                .gender(entity.getGender())
                .email(entity.getEmail())
                .createTime(entity.getCreateTime())
                .createBy(entity.getCreateBy())
                .age(entity.getAge()).build();
    }

}
