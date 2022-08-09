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
 * @Date 2021/5/7
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteSearchDTO {
	private List<QuoteElasticSearchDTO> content;
	private Integer totalPage;
	private Integer totalElements;

	/* 待领取数量统计 */
	private Integer unclaimedCount;

	/* 报价中数量统计 */
	private Integer unquotedCount;

	/* 报价完成数量统计 */
	private Integer quotedCount;
	@Override
	public String toString() {
		return JsonUtil.serializer(this);
	}
}
