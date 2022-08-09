package com.thoth.iqnoon.request;

import com.casstime.commons.utils.JsonUtil;
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
 * @Date 2020/11/9
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("更新ES和新增ES数据入参")
public class DecodeElasticsearchParam {

	@ApiModelProperty("指定ID，inquiryId + resolveBy")
	private String id;
	private String vin;
	private String inquiryId;
	private String resolveBy;
	private String carBrandId;
	private String inquiryType;
	private String userName;
	private String garageCompanyId;
	private String garageCompanyName;
	private String resolveStatusId;
	private String inquiryStatusId;
	private Date resolveEndStamp;
	private Date shoppingListCommitStamp;

	@Override
	public String toString() {
		return JsonUtil.serializer(this);
	}
}
