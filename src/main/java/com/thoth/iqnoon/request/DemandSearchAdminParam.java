package com.thoth.iqnoon.request;

import com.casstime.commons.utils.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author jialin.xie
 * @date 2021/5/14 9:26
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(value = "DemandSearchAdminParam",description = "需求单追踪列表查询条件")
public class DemandSearchAdminParam {

  @ApiModelProperty(value = "需求单id")
  private String demandId;
  @ApiModelProperty(value = "需求单类型（中端车询价SIMPLE_DEMAND）")
  private String demandType;
  @ApiModelProperty(value = "维修厂id")
  private String garageId;
  @ApiModelProperty(value = "创建时间起始时间")
  @NotNull
  private Date startDateStr;
  @ApiModelProperty(value = "创建时间结束时间")
  @NotNull
  private Date endDateStr;
  @ApiModelProperty(value = "需求单状态")
  private String statusId;
  @ApiModelProperty(value = "是否下单")
  private Boolean ordered;
  @ApiModelProperty(value = "汽车品牌id")
  private String carBrandId;
  @ApiModelProperty(value = "询价来源")
  private String sourceId;
  @ApiModelProperty(value = "订单号")
  private String orderId;
  @ApiModelProperty(value = "vin码")
  private String vin;
  @ApiModelProperty(value = "省id")
  private String provinceId;
  @ApiModelProperty(value = "市id")
  private String cityId;
  @ApiModelProperty(value = "区县id")
  private List<String> countyIds;

  @Override
  public String toString() {
    return JsonUtil.serializer(this);
  }
}
