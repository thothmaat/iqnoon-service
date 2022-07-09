package com.thoth.iqnoon.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 分页入参数据封装类
 */
@ApiModel("分页入参数据")
public class PageQuery<T> {
    @ApiModelProperty(value = "当前页码",required = true)
    private Integer pageNum=1;
    @ApiModelProperty(value = "每页查询条目数",required = true)
    private Integer pageSize;

    @ApiModelProperty(value = "分页查询条件参数",required = true)
    @Valid
    @NotNull(message = "查询参数不能为空")
    private T param;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize < 1){
            this.pageSize = 10;
        }else{
            this.pageSize = pageSize;
        }
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
