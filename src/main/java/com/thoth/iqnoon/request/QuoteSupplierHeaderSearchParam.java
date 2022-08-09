package com.thoth.iqnoon.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author cassmall
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "QuoteSupplierHeaderSearchParam",description = "报价单查询条件")
public class QuoteSupplierHeaderSearchParam {

  @ApiModelProperty("询价单Id")
  private String inquiryId;

  @ApiModelProperty("店铺ids")
  private List<String> storeIds;

  @ApiModelProperty(value = "报价单状态",allowableValues = "EXPIRED,QUOTE,RE_DECODE,UNCLAIMED,UNQUOTE,RECEIVE_TIMEOUT,SURE_RECEIPT_TIMEOUT")
  private List<String> statusIds;

  @ApiModelProperty(value = "维修厂Id，传次Id时，都为实名询价")
  private String garageCompanyId;

  @ApiModelProperty(value = "有无内部编码",allowableValues = "Y,N")
  private List<String> internalCodes;

  @ApiModelProperty("创建时间大于")
  private Date createdStartDate;

  @ApiModelProperty("创建时间小于")
  private Date createdEndDate;

  @ApiModelProperty("报价员")
  private List<String> quoteUsers;

  @ApiModelProperty(value = "报价类型",allowableValues = "MANUALLY,ALL,AUTO")
  private List<String> quoteTypes;

  @ApiModelProperty(value = "全部已下单",allowableValues = "Y,N,C")
  private String allOrdered;

  @ApiModelProperty(value = "排序 asc,desc")
  private String order;

  @ApiModelProperty("报价单分配来源")
  private List<String> sourceIds;
  @ApiModelProperty("省ID")
  private List<String> provinceIds;
  @ApiModelProperty("市ID")
  private List<String> cityIds;
  @ApiModelProperty("品牌ID")
  private List<String> carBrandIds;

  @ApiModelProperty(value="询价单类型")
  private List<String> inquiryTypes;

  @ApiModelProperty("标签栏")
  private String navTab;

}
