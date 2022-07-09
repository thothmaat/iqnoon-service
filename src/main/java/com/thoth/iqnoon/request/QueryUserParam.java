package com.thoth.iqnoon.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询用户参数")
public class QueryUserParam {

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("电话号码")
    private String telephone;

    @ApiModelProperty("生份证号码")
    private String identityCardNumber;

    @ApiModelProperty("性别：1-男，0-女")
    private String gender;

    @ApiModelProperty("状态：1-启用，0-禁用")
    private String userStatus;
}
