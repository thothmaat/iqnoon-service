package com.thoth.iqnoon.request;

import com.casstime.commons.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description: 描述信息
 * @Author 何先进
 * @Date 2020/11/10
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DecodeElasticSearchQueryParam {

	private String vin;
	private String inquiryId;
	private String resolveBy;
	private List<String> carBrandIds;
	private String inquiryType;
	private String userName;
	private String garageCompanyId;
	private String garageCompanyName;
	private String decodeStatusId;
	private Date startDate;
	private Date endDate;
	private Date resolvedStartTime;
	private Date resolvedEndTime;
	private Integer page;
	private Integer size;
	@Override
	public String toString() {
		return JsonUtil.serializer(this);
	}
}
