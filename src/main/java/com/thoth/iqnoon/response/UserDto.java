package com.thoth.iqnoon.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@ApiModel("用户信息")
public class UserDto {

    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("电话")
    private String telephone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("状态")
    private String userStatus;
    @ApiModelProperty("身份证号")
    private String identityCardNumber;
    @ApiModelProperty("性别")
    private String gender;
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @ApiModelProperty("最近更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @ApiModelProperty("创建人")
    private String createBy;
    @ApiModelProperty("最近更新人")
    private String updateBy;

}
