package com.thoth.iqnoon.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;

/**
 * 通用请求对象
 */
@ApiModel("通用请求对象")
public class ReqBody<T> {
    @ApiModelProperty("服务名称")
    private String appName;
    @ApiModelProperty("标识")
    private String sign;
    @ApiModelProperty("访问唯一标识")
    private String traceId;
    @Valid
    @ApiModelProperty("请求参数")
    private T param;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
