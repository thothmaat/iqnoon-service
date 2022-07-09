package com.thoth.iqnoon.common;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页数据封装类
 */
@ApiModel("分页返回结果")
public class PageResult<T> {
    @ApiModelProperty("页码")
    private Long pageNum;
    @ApiModelProperty("每页查询条目数")
    private Long pageSize;
    @ApiModelProperty("总页数")
    private Long totalPage;
    @ApiModelProperty("总条目数")
    private Long total;
    @ApiModelProperty("本页查询数据")
    private List<T> rows;

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
