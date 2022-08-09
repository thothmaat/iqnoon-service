package com.thoth.iqnoon.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "QuoteSupplierHeaderSearchDTO",description = "报价单ES查询结果类")
public class QuoteSupplierHeaderSearchDTO {

  @ApiModelProperty("查询条目")
  private List<QuoteSupplierHeaderSearchBaseDTO> quoteSupplierHeaders;

  @ApiModelProperty("总数")
  private Integer total;
}
