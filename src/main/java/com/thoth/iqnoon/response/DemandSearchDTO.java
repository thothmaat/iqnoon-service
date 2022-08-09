package com.thoth.iqnoon.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author jialin.xie
 * @date 2021/5/14 13:42
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "ES需求单分页列表信息",value = "DemandSearchDTO")
public class DemandSearchDTO {

  @ApiModelProperty("需求单分页列表")
  private List<String> demandIds;
  @ApiModelProperty("总记录数")
  private Integer totalElements;
}
