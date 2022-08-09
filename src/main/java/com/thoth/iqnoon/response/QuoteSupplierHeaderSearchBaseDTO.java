package com.thoth.iqnoon.response;

import com.casstime.ec.cloud.inquiry.es.spi.enumeration.DataTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "QuoteSupplierHeaderSearchBaseDTO",description = "报价单查询结果条目")
public class QuoteSupplierHeaderSearchBaseDTO {

  @ApiModelProperty("报价单Id")
  private String quoteSupplierHeaderId;
  @ApiModelProperty("询价单Id")
  private String inquiryId;
  @ApiModelProperty(value = "数据来源",allowableValues = "COLD,HOT")
  private DataTypeEnum dataType;
  @ApiModelProperty("创建时间")
  private Date createdStamp;
}
