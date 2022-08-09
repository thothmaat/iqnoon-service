package com.thoth.iqnoon.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.thoth.iqnoon.request.DecodeElasticSearchQueryParam;
import com.thoth.iqnoon.request.DecodeElasticsearchParam;
import com.thoth.iqnoon.response.DecodeElasticSearchDTO;
import com.thoth.iqnoon.response.DecodeSearchDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @Description: 译码数据查询封装参数工具
 * @Author 何先进
 * @Date 2020/11/10
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DecodeElasticsearchUtil {



	private static final Logger logger = LoggerFactory.getLogger(DecodeElasticsearchUtil.class);
	private static final String RESOLVE_END_STAMP = "resolveEndStamp";
	private static final String RESOLVE_BY = "resolveBy";
	private static final String GARAGE_COMPANY_ID = "garageCompanyId";
	private static final String INQUIRY_ID = "inquiryId";
	private static final String RESOLVE_STATUS_ID = "resolveStatusId";
	private static final String VIN = "vin";
	private static final String INQUIRY_STATUS_ID = "inquiryStatusId";
	private static final String ES_ID = "id";
	private static final String CAR_BRAND_ID = "carBrandId";
	private static final String INQUIRY_TYPE = "inquiryType";
	private static final String USER_NAME = "userName";
	private static final String GARAGE_COMPANY_NAME ="garageCompanyName";
	private static final String SHOPPING_LIST_COMMIT_STAMP = "shoppingListCommitStamp";

	private static final String ES_SOURCE_COLUMN = "_source";
	private static final String ES_ERROR_MESSAGE = "es 查询结果解析错误";
	private static final String DECODED = "DECODED";
	private static final String STAR = "*";
	private static final String IN_THE_DECODING = "IN_THE_DECODING";
	private static final String COLLAPSE = ",\"collapse\": { \"field\": \"inquiryId\"}}";

	public static String convertDecodeQueryParamToQueryBuilder(
		DecodeElasticSearchQueryParam param){
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if(StringUtils.isNotBlank(param.getInquiryId())){
			queryBuilder.filter(QueryBuilders.termQuery(INQUIRY_ID,param.getInquiryId()));
		}
		if(StringUtils.isNotBlank(param.getVin())){
			queryBuilder.filter(QueryBuilders.wildcardQuery(VIN,STAR+param.getVin()+STAR));
		}
		if(StringUtils.isNotBlank(param.getGarageCompanyName())){
			queryBuilder.filter(QueryBuilders.wildcardQuery(GARAGE_COMPANY_NAME,STAR+param.getGarageCompanyName()+STAR));
		}
		if(StringUtils.isNotBlank(param.getUserName())){
			queryBuilder.filter(QueryBuilders.wildcardQuery(USER_NAME,STAR+param.getUserName()+STAR));
		}
		if(StringUtils.isNotBlank(param.getResolveBy())){
			queryBuilder.filter(QueryBuilders.termsQuery(RESOLVE_BY,getResolveBySet(param.getDecodeStatusId(),param.getResolveBy())));
		}
		if(ObjectUtils.isNotEmpty(param.getCarBrandIds())){
			queryBuilder.filter(QueryBuilders.termsQuery(CAR_BRAND_ID,param.getCarBrandIds()));
		}
		if(StringUtils.isNotBlank(param.getInquiryType())){
			queryBuilder.filter(QueryBuilders.termQuery(INQUIRY_TYPE,param.getInquiryType()));
		}
		if(StringUtils.isNotBlank(param.getDecodeStatusId())){
			List<String> statusList = getStatusSet(param.getDecodeStatusId());
			if(ObjectUtils.isNotEmpty(statusList)){
				queryBuilder.filter(QueryBuilders.termsQuery(RESOLVE_STATUS_ID,statusList));
			}
		}
		if(StringUtils.isNotBlank(param.getDecodeStatusId())){
			queryBuilder.filter(QueryBuilders.termsQuery(INQUIRY_STATUS_ID,getInquiryStatusIds(param.getDecodeStatusId())));
		}
		if(StringUtils.isBlank(param.getInquiryId())){
			if(null != param.getStartDate()){
				queryBuilder.filter(
					QueryBuilders.rangeQuery(SHOPPING_LIST_COMMIT_STAMP)
						.gte(param.getStartDate().getTime())
						.boost(0f));
			}
			if(null != param.getEndDate()){
				queryBuilder.filter(
					QueryBuilders.rangeQuery(SHOPPING_LIST_COMMIT_STAMP)
						.lt(param.getEndDate().getTime())
						.boost(0f));
			}
			if(null != param.getResolvedStartTime()){
				queryBuilder.filter(
					QueryBuilders.rangeQuery(RESOLVE_END_STAMP)
						.gte(param.getResolvedStartTime().getTime())
						.boost(0f));
			}
			if(null != param.getResolvedEndTime()){
				queryBuilder.filter(
					QueryBuilders.rangeQuery(RESOLVE_END_STAMP)
						.lt(param.getResolvedEndTime().getTime())
						.boost(0f));
			}
		}
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		if(null == param.getSize() || 0 == param.getSize()){
			param.setSize(50);
		}
		if(null == param.getPage() || 0 == param.getPage()){
			param.setPage(1);
		}
		CardinalityAggregationBuilder fieldCardinality = AggregationBuilders.cardinality("total_count").field(INQUIRY_ID);
		searchSourceBuilder.query(queryBuilder)
			.aggregation(fieldCardinality)
			.from((param.getPage() - 1) * param.getSize())
			.size(param.getSize());
		searchSourceBuilder.sort(getSortName(param), getOrder(param));
		String queryStr = searchSourceBuilder.toString();
		return queryStr.substring(0,queryStr.length()-1)+COLLAPSE;
	}

	private static String getSortName(DecodeElasticSearchQueryParam param) {
		return DECODED.equalsIgnoreCase(param.getDecodeStatusId())
			? RESOLVE_END_STAMP : SHOPPING_LIST_COMMIT_STAMP;
	}

	private static SortOrder getOrder(DecodeElasticSearchQueryParam param) {
		return
			IN_THE_DECODING.equalsIgnoreCase(param.getDecodeStatusId()) ? SortOrder.ASC:SortOrder.DESC;
	}
	private static List<String> getInquiryStatusIds(String statusId) {
		List<String> statusIds = Lists.newArrayList();
		if(IN_THE_DECODING.equalsIgnoreCase(statusId)){
			// 'DECODED','UNCLAIMED','UNQUOTE'

		}else{
			//'ABATE','DECODED','DRAFT','QUOTE','RETURNED','UNCLAIMED','UNQUOTE'

		}
		return statusIds;
	}

	private static List<String> getStatusSet(String statusId){
		List<String> statusList = Lists.newArrayList();
		if(StringUtils.isNotBlank(statusId)){
			if(DECODED.equalsIgnoreCase(statusId)){
			}else if(IN_THE_DECODING.equalsIgnoreCase(statusId)){
				statusList.add(IN_THE_DECODING);
			}else{
				statusList.add(IN_THE_DECODING);
			}
		}
		return statusList;
	}
	private static List<String> getResolveBySet(String statusId,String resolveBy){
		List<String> resolvedList = Lists.newArrayList();
		if(StringUtils.isNotBlank(statusId)){
			resolvedList.add(resolveBy);
			if(!DECODED.equalsIgnoreCase(statusId)){
				resolvedList.add("system");
			}
		}
		return resolvedList;
	}
	public static DecodeSearchDTO convertJsonResponseToDecodeListPage(String jsonResponse) {
		List<DecodeElasticSearchDTO> decodeQueryDTOList = Lists.newArrayList();
		DecodeSearchDTO decodeSearchDTO = new DecodeSearchDTO();
		try {
			JSONObject object = JSON.parseObject(jsonResponse);
			if(null == object){
				return decodeSearchDTO;
			}
			String totalCount = getTotalCount(object);
			JSONObject hits = object.getJSONObject("hits");
			if(null == hits){
				return decodeSearchDTO;
			}
			JSONArray hitsList = hits.getJSONArray("hits");
			decodeSearchDTO.setTotalElements(Integer.valueOf(totalCount));
			if (hitsList != null && !hitsList.isEmpty()) {
				for (int i = 0; i < hitsList.size(); i++) {
					JSONObject hit = hitsList.getJSONObject(i);
					JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
					DecodeElasticSearchDTO decodeQueryDTO = new DecodeElasticSearchDTO();
					decodeQueryDTO.setId(response.getString(ES_ID));
					decodeQueryDTO.setGarageCompanyId(response.getString(GARAGE_COMPANY_ID));
					decodeQueryDTO.setGarageCompanyName(response.getString(GARAGE_COMPANY_NAME));
					decodeQueryDTO.setInquiryId(response.getString(INQUIRY_ID));
					decodeQueryDTO.setInquiryType(response.getString(INQUIRY_TYPE));
					decodeQueryDTO.setResolveBy(response.getString(RESOLVE_BY));
					decodeQueryDTO.setResolveEndStamp(response.getDate(RESOLVE_END_STAMP));
					decodeQueryDTO.setShoppingListCommitStamp(response.getDate(SHOPPING_LIST_COMMIT_STAMP));
					decodeQueryDTO.setCarBrandId(response.getString(CAR_BRAND_ID));
					decodeQueryDTO.setUserName(response.getString(USER_NAME));
					decodeQueryDTO.setResolveStatusId(response.getString(RESOLVE_STATUS_ID));
					decodeQueryDTO.setVin(response.getString(VIN));
					decodeQueryDTO.setInquiryStatusId(response.getString(INQUIRY_STATUS_ID));
					decodeQueryDTOList.add(decodeQueryDTO);
				}
			}
			decodeSearchDTO.setContent(decodeQueryDTOList);
		} catch (Exception e) {
			logger.error(ES_ERROR_MESSAGE, e);
		}
		return decodeSearchDTO;
	}

	private static String getTotalCount(JSONObject object) throws JSONException {
		String totalCount = "0";
		if(null == object){
			return totalCount;
		}
		JSONObject aggregations = object.getJSONObject("aggregations");
		if(null == aggregations){
			return totalCount;
		}
		JSONObject totalCountObj = aggregations.getJSONObject("total_count");
		if(null == totalCountObj){
			return totalCount;
		}
		String count = totalCountObj.getString("value");
		if(StringUtils.isBlank(count)){
			count = totalCount;
		}
		return count;
	}

	public static String buildQueryExistDataBuilder(Set<String> ids){
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		if(ObjectUtils.isNotEmpty(ids)){
			queryBuilder.filter(QueryBuilders.termsQuery(ES_ID,ids));
		}
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilder);
		return searchSourceBuilder.toString();
	}


	public static String toDecodeBatchJSON(List<DecodeElasticsearchParam> list, String indexName, String typeName) {
		StringBuilder sb = new StringBuilder();
		for (DecodeElasticsearchParam full : list) {
			String str = getDecodeEsSaveString(indexName, typeName, full,
				"{ \"index\" : { \"_index\" : \"");
			sb.append(str)
				.append("\n")
				.append(full)
				.append("\n");
		}
		return sb.toString();
	}

	public static String toDecodeBatchUpdateJSon(List<DecodeElasticsearchParam> list, String indexName, String typeName) {
		StringBuilder sb = new StringBuilder();
		for (DecodeElasticsearchParam full : list) {
			String str = getDecodeEsSaveString(indexName, typeName, full,
				"{ \"update\" : { \"_index\" : \"");
			sb.append(str)
				.append("\n")
				.append("{ \"doc\":").append(full).append("}")
				.append("\n");
		}
		return sb.toString();
	}

	private static String getDecodeEsSaveString(String indexName, String typeName,
		DecodeElasticsearchParam full, String s) {
		return s + indexName + "\", \"_type\" : \"" + typeName + "\", \"_id\" : \"" + full.getId() + "\" } }";
	}
}
