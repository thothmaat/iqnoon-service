package com.thoth.iqnoon.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@ApiModel("用户信息")
public class UserDto {

    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("电话")
    private String telephone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("状态")
    private String userStatus;
    @ApiModelProperty("身份证号")
    private String idCardNumber;
    @ApiModelProperty("微信号")
    private String weChat;
    @ApiModelProperty(value = "性别",allowableValues = "0,1,2")
    private String gender;
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("出生年月日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
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
    @ApiModelProperty("删除标记（0-未删除，1-删除）")
    private String deleteFlag;

}
