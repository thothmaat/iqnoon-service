package com.thoth.iqnoon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Description: 描述信息
 * @Author 何先进
 * @Date 2021/5/18
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class EsQuotationUtil {

	private static final String ES_SOURCE_COLUMN = "_source";
	private static final String HITS = "hits";
	private static final String QUOTATION_ID = "id";
	public static List<String> convertJsonResponseToQuotationIds(String jsonResponse) {
		List<String> quotationIds = Lists.newArrayList();
		if(StringUtils.isBlank(jsonResponse)){
			return quotationIds;
		}
		try {
			JSONObject object = JSON.parseObject(jsonResponse);
			if(null == object || null == object.getJSONObject(HITS)){
				return quotationIds;
			}
			JSONObject hits = object.getJSONObject(HITS);
			JSONArray hitsList = hits.getJSONArray(HITS);
			if (hitsList != null && !hitsList.isEmpty()) {
				for (int i = 0; i < hitsList.size(); i++) {
					JSONObject hit = hitsList.getJSONObject(i);
					JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
					quotationIds.add(response.getLong(QUOTATION_ID).toString());
				}
			}
		} catch (Exception e) {
			log.error("查询失败", e);
		}
		return quotationIds;
	}
}
