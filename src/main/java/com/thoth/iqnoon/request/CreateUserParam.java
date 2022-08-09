package com.thoth.iqnoon.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@ApiModel("新增用户信息")
public class CreateUserParam {

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @ApiModelProperty(value = "用户密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "电话",required = true)
    private String telephone;
    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    @ApiModelProperty("微信号")
    private String weChat;
    @ApiModelProperty("状态0-禁用，1-启用")
    private String userStatus;
    @ApiModelProperty("身份证号")
    @NotBlank(message = "生份证号不能为空")
    private String idCardNumber;
    @ApiModelProperty(value = "性别",allowableValues = "0,1,2")
    @NotBlank(message = "性别不能为空")
    private String gender;
    @ApiModelProperty("年龄")
    private Integer age;
    @ApiModelProperty("创建人")
    private String createBy;

}
