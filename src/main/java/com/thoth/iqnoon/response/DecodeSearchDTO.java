package com.thoth.iqnoon.response;

import com.casstime.commons.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 描述信息
 * @Author 何先进
 * @Date 2020/11/11
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecodeSearchDTO {

	private List<DecodeElasticSearchDTO> content;
	private Integer totalPage;
	private Integer totalElements;
	@Override
	public String toString() {
		return JsonUtil.serializer(this);
	}
}
