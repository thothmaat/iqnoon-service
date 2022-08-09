package com.thoth.iqnoon.response;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: 描述信息
 * @Author 何先进
 * @Date 2021/5/7
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "QuoteElasticSearchDTO",description = "报价单查询结果条目")
public class QuoteElasticSearchDTO{
	@ApiModelProperty("报价单ID")
	private Long id;
	@ApiModelProperty("询价单ID")
	private String inquiryId;
	@ApiModelProperty("收货地址市")
	private String cityId;
	@ApiModelProperty("收货地址省")
	private String provinceId;
	@ApiModelProperty("创建时间")
	private String carBrandId;
	@ApiModelProperty("询价类型")
	private String inquiryType;
	@ApiModelProperty("报价类型")
	private String quoteType;
	@ApiModelProperty("店铺ID")
	private String storeId;
	@ApiModelProperty("报价员")
	private String quoteUser;
	@ApiModelProperty("状态")
	private String statusId;
	@ApiModelProperty("全部下单")
	private String allOrdered;
	@ApiModelProperty("维修厂")
	private String garageCompanyId;
	@ApiModelProperty("数据源")
	private String dataSource;
	@ApiModelProperty("是否匿名")
	private Integer isAnonymous;
	@ApiModelProperty("来源")
	private String sourceId;
	@ApiModelProperty("是否是中端车")
	private Boolean isSimpleInquiry;
	@ApiModelProperty
	private String inquiryDataType;
	@ApiModelProperty("创建时间")
	private Date createdStamp;
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
