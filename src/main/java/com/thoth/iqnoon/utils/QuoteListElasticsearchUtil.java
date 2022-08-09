package com.thoth.iqnoon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.thoth.iqnoon.request.QuoteSupplierHeaderSearchParam;
import com.thoth.iqnoon.response.QuoteElasticSearchDTO;
import com.thoth.iqnoon.response.QuoteSearchDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description: 描述信息
 * @Author 何先进
 * @Date 2021/5/7
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuoteListElasticsearchUtil {

	private static final Logger logger = LoggerFactory.getLogger(QuoteListElasticsearchUtil.class);
	private static final String CREATED_STAMP = "createdStamp";
	private static final String GARAGE_COMPANY_ID = "garageCompanyId";
	private static final String INQUIRY_ID = "inquiryId";
	private static final String PROVINCE_ID = "provinceId";
	private static final String CITY_ID = "cityId";
	private static final String CAR_BRAND_ID = "carBrandId";
	private static final String STATUS_ID = "statusId";
	private static final String ALL_ORDERED = "allOrdered";
	private static final String SOURCE_ID ="sourceId";
	private static final String ES_SOURCE_COLUMN = "_source";
	private static final String ES_DATA_SOURCE_COLUMN = "dataSource";
	private static final String ES_ERROR_MESSAGE = "es 查询报价列表结果解析错误";
	private static final int INQUIRY_ID_LENGTH = 12;
	private static final String STORE_ID = "storeId";
	private static final String QUOTE_USER = "quoteUser";
	private static final String INQUIRY_TYPE = "inquiryType";
	private static final String QUOTE_TYPE = "quoteType";
	private static final String IS_ANONYMOUS = "isAnonymous";
	private static final String IS_SIMPLE_INQUIRY = "isSimpleInquiry";
	private static final String ID = "id";
	private static final String STAR = "*";
	private static final String HITS = "hits";
	private static final String TOTAL = "total";
	private static final String STATUS_ID_INVALID = "INVALID";

	/**
	 * 选择全部页签时的nav tab 值
	 */
	private static final String NAV_TAB_ALL = "ALL";

	public static BoolQueryBuilder convertQueryParamToQueryBuilder(
			QuoteSupplierHeaderSearchParam param) {
		BoolQueryBuilder queryBuilder = convertBasicQueryParamToQueryBuilder(param);
		addStatusIdConditionByNavTab(param, queryBuilder);
		queryBuilder.mustNot(QueryBuilders.termQuery(STATUS_ID,STATUS_ID_INVALID));
		return queryBuilder;
	}

	/**
	 * 转换基础查询参数
	 */
	public static BoolQueryBuilder convertBasicQueryParamToQueryBuilder(QuoteSupplierHeaderSearchParam param){
		BoolQueryBuilder queryBuilder =  QueryBuilders.boolQuery();
		boolean isNeedOtherParam = true;
		//支持询价单号模糊查询
		if (StringUtils.isNotBlank(param.getInquiryId())) {
			if (param.getInquiryId().length() == INQUIRY_ID_LENGTH) {
				queryBuilder.filter(QueryBuilders.termQuery(INQUIRY_ID,param.getInquiryId()));
			}else{
				queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getInquiryId()).field(INQUIRY_ID));
			}
			isNeedOtherParam = false;
		}
		//报价单 报价店铺过滤
		if (ObjectUtils.isNotEmpty(param.getStoreIds())) {
			queryBuilder.filter(QueryBuilders.termsQuery(STORE_ID, param.getStoreIds()));
		}

		if(isNeedOtherParam){
			//报价单 报价范围类型过滤
			if (ObjectUtils.isNotEmpty(param.getSourceIds())) {
				queryBuilder.filter(QueryBuilders.termsQuery(SOURCE_ID, param.getSourceIds()));
			}
			//省过滤
			if (ObjectUtils.isNotEmpty(param.getProvinceIds())) {
				queryBuilder.filter(QueryBuilders.termsQuery(PROVINCE_ID, param.getProvinceIds()));
			}
			//市过滤
			if (ObjectUtils.isNotEmpty(param.getCityIds())) {
				queryBuilder.filter(QueryBuilders.termsQuery(CITY_ID, param.getCityIds()));
			}
			//品牌过滤
			if (ObjectUtils.isNotEmpty(param.getCarBrandIds())) {
				queryBuilder.filter(QueryBuilders.termsQuery(CAR_BRAND_ID, param.getCarBrandIds()));
			}
			//报价员过滤
			if (ObjectUtils.isNotEmpty(param.getQuoteUsers())) {
				queryBuilder.filter(QueryBuilders.termsQuery(QUOTE_USER, param.getQuoteUsers()));
			}
			//报价类型过滤
			if (ObjectUtils.isNotEmpty(param.getQuoteTypes())) {
				queryBuilder.filter(QueryBuilders.termsQuery(QUOTE_TYPE, param.getQuoteTypes()));
			}
			//询价类型过滤
			if(ObjectUtils.isNotEmpty(param.getInquiryTypes())){
				queryBuilder.filter(QueryBuilders.termsQuery(INQUIRY_TYPE,param.getInquiryTypes()));
			}
			//维修厂过滤
			if (StringUtils.isNotBlank(param.getGarageCompanyId())) {
				queryBuilder.filter(QueryBuilders.termQuery(GARAGE_COMPANY_ID,param.getGarageCompanyId()))
						.filter(QueryBuilders.termQuery(IS_ANONYMOUS,0));
			}
			//是否下单过滤
			if (StringUtils.isNotBlank(param.getAllOrdered())) {
				queryBuilder.filter(QueryBuilders.termQuery(ALL_ORDERED,param.getAllOrdered()));
			}
			//派单开始时间限制
			if (param.getCreatedStartDate() != null) {
				queryBuilder.filter(
						QueryBuilders.rangeQuery(CREATED_STAMP).gt(param.getCreatedStartDate().getTime())
								.boost(0f));
			}
			//派单截止时间限制
			if (param.getCreatedEndDate() != null) {
				queryBuilder.filter(
						QueryBuilders.rangeQuery(CREATED_STAMP).lt(param.getCreatedEndDate().getTime())
								.boost(0f));
			}
		}
		return queryBuilder;
	}


	/**
	 * 通过选择的navTab 添加statusId属性
	 */
	private static void addStatusIdConditionByNavTab(QuoteSupplierHeaderSearchParam param,
			BoolQueryBuilder queryBuilder) {

		/* 根据navTab 确定 报价状态的查询statusId 的值进行查询*/

		queryBuilder.filter(QueryBuilders
				.termsQuery(STATUS_ID, param.getStatusIds()));

	}

	/**
	 * 获取totalElement
	 */
	public static Integer getElementCount(String jsonStr) {
		try {
			JSONObject object = JSON.parseObject(jsonStr);
			JSONObject hitsObject = object.getJSONObject(HITS);
			return hitsObject.getInteger(TOTAL);
		} catch (Exception e) {
			logger.error("GET_TOTAL_COUNT_FROM_ES_RESULT_FAILED 从数据中获取统计数量失败 jsonStr {}", jsonStr, e);
			return 0;
		}
	}

	public static QuoteSearchDTO convertJsonResponseToQuoteListInfo(String jsonResponse) {
		QuoteSearchDTO quoteSearchDTO = new QuoteSearchDTO();
		List<QuoteElasticSearchDTO> res = Lists.newArrayList();
		try {
			JSONObject object = JSON.parseObject(jsonResponse);
			JSONObject hits = object.getJSONObject(HITS);
			JSONArray hitsList = hits.getJSONArray(HITS);
			quoteSearchDTO.setTotalElements(hits.getInteger(TOTAL));
			if (hitsList != null && !hitsList.isEmpty()) {
				for (int i = 0; i < hitsList.size(); i++) {
					JSONObject hit = hitsList.getJSONObject(i);
					JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
					QuoteElasticSearchDTO quoteElasticSearchDTO = new QuoteElasticSearchDTO();
					quoteElasticSearchDTO.setCreatedStamp(response.getDate(CREATED_STAMP));
					quoteElasticSearchDTO.setInquiryId(response.getString(INQUIRY_ID));
					quoteElasticSearchDTO.setStoreId(response.getString(STORE_ID));
					quoteElasticSearchDTO.setStatusId(response.getString(STATUS_ID));
					quoteElasticSearchDTO.setQuoteType(response.getString(QUOTE_TYPE));
					quoteElasticSearchDTO.setQuoteUser(response.getString(QUOTE_USER));
					quoteElasticSearchDTO.setGarageCompanyId(response.getString(GARAGE_COMPANY_ID));
					quoteElasticSearchDTO.setAllOrdered(response.getString(ALL_ORDERED));
					quoteElasticSearchDTO.setId(response.getLong(ID));
					quoteElasticSearchDTO.setIsAnonymous(response.getInteger(IS_ANONYMOUS));
					quoteElasticSearchDTO.setDataSource(response.getString(ES_DATA_SOURCE_COLUMN));
					quoteElasticSearchDTO.setSourceId(response.getString(SOURCE_ID));
					quoteElasticSearchDTO.setInquiryType(response.getString(INQUIRY_TYPE));
					quoteElasticSearchDTO.setIsSimpleInquiry(response.getBoolean(IS_SIMPLE_INQUIRY));
					quoteElasticSearchDTO.setInquiryDataType(response.getString(ES_DATA_SOURCE_COLUMN));
					res.add(quoteElasticSearchDTO);
				}
			}
		} catch (Exception e) {
			logger.error(ES_ERROR_MESSAGE, e);
		}
		quoteSearchDTO.setContent(res);
		return quoteSearchDTO;
	}

}
