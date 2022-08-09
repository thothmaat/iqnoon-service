package com.thoth.iqnoon.response;

import com.casstime.commons.model.Model;
import com.casstime.commons.utils.JsonUtil;
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
public class DecodeElasticSearchDTO implements Model {

	private String id;
	private String vin;
	private String inquiryId;
	private String inquiryType;
	private String resolveBy;
	private String carBrandId;
	private String garageCompanyName;
	private String userName;
	private String garageCompanyId;
	private String resolveStatusId;
	private String inquiryStatusId;
	private Date shoppingListCommitStamp;
	private Date resolveEndStamp;

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public String toString() {
		return JsonUtil.serializer(this);
	}
}
