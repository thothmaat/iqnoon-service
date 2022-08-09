package com.thoth.iqnoon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.casstime.ec.cloud.inquiry.es.spi.dto.*;
import com.casstime.ec.cloud.inquiry.es.spi.enumeration.DataTypeEnum;
import com.casstime.ec.cloud.inquiry.es.spi.enumeration.InquiryDataTypeEnum;
import com.casstime.ec.cloud.inquiry.es.spi.model.EsInquiryBase;
import com.casstime.ec.cloud.inquiry.es.spi.param.DemandSearchAdminParam;
import com.casstime.ec.cloud.inquiry.es.spi.param.InquirySearchConditionParam;
import com.casstime.ec.cloud.inquiry.es.spi.param.InquirySearchConditionParam.CommonRoleCondition;
import com.casstime.ec.cloud.inquiry.es.spi.param.InquirySearchPcConditionParam;
import com.casstime.ec.cloud.inquiry.es.spi.param.QuoteSupplierHeaderSearchParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: 毕巍巍
 * @Date: 2019/10/25 15:56
 * @Description:
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ElasticSearchUtil {

  private static final Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
  private static final String CREATED_STAMP = "createdStamp";
  private static final String CREATED_BY = "createdBy";
  private static final String GARAGE_COMPANY_ID = "garageCompanyId";
  private static final String INQUIRY_ID = "inquiryId";
  private static final String STATUS_ID = "statusId";

  private static final String ALL_ORDERED = "allOrdered";
  private static final String SHOPPING_LIST_ID = "shoppingListId";
  private static final String VIN = "vin";
  private static final String CAR_BRAND_ID = "carBrandId";
  private static final String NEEDS_NAMES_ZH = "needsNamesZh";
  private static final String NEEDS_NAMES_EN = "needsNamesEn";
  private static final String ORDER_IDS = "orderIds";
  private static final String INQUIRY_TYPE = "inquiryType";
  private static final String PROVINCE_GEO_ID = "provinceGeoId";
  private static final String CITY_GEO_ID = "cityGeoId";
  private static final String COUNTY_GEO_ID = "countyGeoId";
  private static final String VILLAGE_GEO_ID = "villageGeoId";



  private static final String WHITESPACE = "whitespace";
  private static final String SOURCE_ID ="sourceId";
  private static final String SHOPPING_LIST_ID_BACK ="shoppingListId.back";
  private static final String ES_SOURCE_COLUMN = "_source";
  private static final String ES_DATA_SOURCE_COLUMN = "dataSource";
  private static final String ES_ERROR_MESSAGE = "es 查询结果解析错误";
  private static final String HITS = "hits";
  private static final String TOTAL = "total";

  private static final String ALL_PROVINCE_ID = "0";
  private static final String ALL_CITY_ID = "0-0";
  private static final int DEMAND_ID_LENGTH = 12;

  private static BoolQueryBuilder convertInquiryQueryParamToQueryBuilder(InquirySearchConditionParam param){
    CommonRoleCondition commonRoleCondition = Optional.ofNullable(param.getCommonRoleCondition())
        .orElse(new CommonRoleCondition());
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    if (CollectionUtils.isNotEmpty(param.getStatusIds())) {
      queryBuilder.filter(QueryBuilders.termsQuery(STATUS_ID, param.getStatusIds()));
    }
    if (CollectionUtils.isNotEmpty(param.getCarBrandIds())) {
      queryBuilder.filter(QueryBuilders.termsQuery(CAR_BRAND_ID, param.getCarBrandIds()));
    }
    if (param.getCreatedStartDate() != null) {
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).gt(param.getCreatedStartDate().getTime())
              .boost(0f));
    }else {
      long createdStartTime = LocalDateTime.now().minusYears(1).toInstant(ZoneOffset.of("+8")).toEpochMilli();
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).gt(createdStartTime)
              .boost(0f));
    }
    if (param.getCreatedEndDate() != null) {
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).lt(param.getCreatedEndDate().getTime())
              .boost(0f));
    }

    if (CollectionUtils.isNotEmpty(commonRoleCondition.getCreatedBys()) && CollectionUtils.isNotEmpty(param.getAdminCreateBys())) {
      BoolQueryBuilder outerBoolQuery = QueryBuilders.boolQuery();
      BoolQueryBuilder innerBoolQuery = QueryBuilders.boolQuery();
      innerBoolQuery
          .filter(QueryBuilders.termsQuery(CREATED_BY, commonRoleCondition.getCreatedBys()))
          .filter(QueryBuilders.termQuery(GARAGE_COMPANY_ID, commonRoleCondition.getGarageCompanyId()));

      BoolQueryBuilder innerBoolQueryAdmin = QueryBuilders.boolQuery();
      innerBoolQueryAdmin
          .filter(QueryBuilders.termsQuery(CREATED_BY, param.getAdminCreateBys()))
          .filter(QueryBuilders.termsQuery(GARAGE_COMPANY_ID, param.getAdminCompanyIds()));
      outerBoolQuery.should(innerBoolQueryAdmin).should(innerBoolQuery);

      queryBuilder.filter(outerBoolQuery);
    }else if(CollectionUtils.isNotEmpty(commonRoleCondition.getCreatedBys())){
      queryBuilder.filter(QueryBuilders.termQuery(GARAGE_COMPANY_ID,commonRoleCondition.getGarageCompanyId()))
          .filter(QueryBuilders.termsQuery(CREATED_BY,commonRoleCondition.getCreatedBys()));
    }else if(CollectionUtils.isNotEmpty(param.getAdminCreateBys())){
      queryBuilder
          .filter(QueryBuilders.termsQuery(CREATED_BY,param.getAdminCreateBys()))
          .filter(QueryBuilders.termsQuery(GARAGE_COMPANY_ID, param.getAdminCompanyIds()));
    }

    if (StringUtils.isNotBlank(param.getSearchContext())) {
      queryBuilder.filter(
          QueryBuilders.multiMatchQuery(param.getSearchContext())
              .field(SHOPPING_LIST_ID).field(SHOPPING_LIST_ID_BACK)
              .field(NEEDS_NAMES_ZH)
              .field(NEEDS_NAMES_EN).field("needsNamesEn.back")
              .field(VIN).field("vin.back").analyzer(WHITESPACE));
    }
    queryBuilder.filter(QueryBuilders.termQuery(ES_DATA_SOURCE_COLUMN, "HOT"));
    return queryBuilder;
  }
  private static BoolQueryBuilder convertQueryParamToQueryBuilder(InquirySearchPcConditionParam param){
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    if(StringUtils.isNotBlank(param.getInquiryId())){
      queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getInquiryId()).field(SHOPPING_LIST_ID).field(SHOPPING_LIST_ID_BACK).analyzer(WHITESPACE));
    }
    if(StringUtils.isNotBlank(param.getCarBrandId())){
      queryBuilder.filter(QueryBuilders.termQuery(CAR_BRAND_ID,param.getCarBrandId()));
    }
    if(StringUtils.isNotBlank(param.getVin())){
      queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getVin()).field("vin").field("vin.back").analyzer(WHITESPACE));
    }
    if(CollectionUtils.isNotEmpty(param.getStatusIds())){
      queryBuilder.filter(QueryBuilders.termsQuery(STATUS_ID,param.getStatusIds()));
    }
    if(StringUtils.isNotBlank(param.getUserNeed())){
      queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getUserNeed()).field(NEEDS_NAMES_ZH).field(NEEDS_NAMES_EN).field("needsNamesEn.back").analyzer(WHITESPACE));
    }
    if (param.getCreatedStartDate() != null) {
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).gt(param.getCreatedStartDate().getTime()));
    }
    if (param.getCreatedEndDate() != null) {
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).lt(param.getCreatedEndDate().getTime()));
    }
    if(StringUtils.isNotBlank(param.getOrderId())){
      queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getOrderId()).field(ORDER_IDS).field("orderIds.back").analyzer(WHITESPACE));
    }

    if (CollectionUtils.isNotEmpty(param.getCreatedBys()) && CollectionUtils.isNotEmpty(param.getAdminCreateBys())) {
      BoolQueryBuilder outerBoolQuery = QueryBuilders.boolQuery();
      BoolQueryBuilder innerBoolQuery = QueryBuilders.boolQuery();
      innerBoolQuery
          .filter(QueryBuilders.termsQuery(CREATED_BY, param.getCreatedBys()))
          .filter(QueryBuilders.termsQuery(GARAGE_COMPANY_ID, param.getGarageCompanyIds()));
      outerBoolQuery.should(QueryBuilders.termsQuery(CREATED_BY, param.getAdminCreateBys())).should(innerBoolQuery);
      queryBuilder.filter(outerBoolQuery);
    }else if(CollectionUtils.isNotEmpty(param.getCreatedBys())){
      queryBuilder.filter(QueryBuilders.termsQuery(GARAGE_COMPANY_ID,param.getGarageCompanyIds()))
          .filter(QueryBuilders.termsQuery(CREATED_BY,param.getCreatedBys()));
    }else if(CollectionUtils.isNotEmpty(param.getAdminCreateBys())){
      queryBuilder
          .filter(QueryBuilders.termsQuery(CREATED_BY,param.getAdminCreateBys()));
    }
    return queryBuilder;
  }




  private static BoolQueryBuilder convertQuoteSupplierHeaderQueryParamToQueryBuilder(
      QuoteSupplierHeaderSearchParam param){

    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

    if (CollectionUtils.isNotEmpty(param.getStoreIds())) {
      queryBuilder.filter(QueryBuilders.termsQuery("storeId", param.getStoreIds()));
    }

    if (CollectionUtils.isNotEmpty(param.getQuoteUsers())) {
      queryBuilder.filter(QueryBuilders.termsQuery("quoteUser", param.getQuoteUsers()));
    }

    if (StringUtils.isNotBlank(param.getInquiryId())) {
      if (param.getInquiryId().length() == 12) {
        queryBuilder.filter(QueryBuilders.termQuery(INQUIRY_ID,param.getInquiryId()));
        return queryBuilder;
      }else{
        queryBuilder.filter(
            QueryBuilders.multiMatchQuery(param.getInquiryId())
                .field(INQUIRY_ID));
      }
    }

    if (CollectionUtils.isNotEmpty(param.getInternalCodes())) {
      queryBuilder.filter(QueryBuilders.termsQuery("internalCode", param.getInternalCodes()));
    }

    if (CollectionUtils.isNotEmpty(param.getQuoteTypes())) {
      queryBuilder.filter(QueryBuilders.termsQuery("quoteType", param.getQuoteTypes()));
    }



    if (StringUtils.isNotBlank(param.getGarageCompanyId())) {
      queryBuilder.filter(QueryBuilders.termQuery(GARAGE_COMPANY_ID,param.getGarageCompanyId()))
          .filter(QueryBuilders.termQuery("isAnonymous",0));
    }


    if (StringUtils.isNotBlank(param.getAllOrdered())) {
      queryBuilder.filter(QueryBuilders.termQuery(ALL_ORDERED,param.getAllOrdered()));
    }

    if (CollectionUtils.isNotEmpty(param.getStatusIds())) {
      queryBuilder.filter(QueryBuilders.termsQuery(STATUS_ID, param.getStatusIds()));
    }

    if (param.getCreatedStartDate() != null) {
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).gt(param.getCreatedStartDate().getTime())
              .boost(0f));
    }
    if (param.getCreatedEndDate() != null) {
      queryBuilder.filter(
          QueryBuilders.rangeQuery(CREATED_STAMP).lt(param.getCreatedEndDate().getTime())
              .boost(0f));
    }

    if(CollectionUtils.isNotEmpty(param.getSourceIds())){
      queryBuilder.filter(QueryBuilders.termsQuery(SOURCE_ID,param.getSourceIds()));
    }

    return queryBuilder;
  }

  private static BoolQueryBuilder convertTrackParamToQueryBuilder(DemandSearchAdminParam param){

    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

    if (StringUtils.isNotBlank(param.getDemandId()) && param.getDemandId().length() == DEMAND_ID_LENGTH) {
      queryBuilder.filter(QueryBuilders.termQuery(SHOPPING_LIST_ID,param.getDemandId()));
      return queryBuilder;
    }
    if(StringUtils.isNotBlank(param.getOrderId())){
      queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getOrderId()).field(ORDER_IDS).field("orderIds.back").analyzer(WHITESPACE));
      return queryBuilder;
    }
    if (StringUtils.isNotBlank(param.getDemandId())) {
      queryBuilder.filter(QueryBuilders.multiMatchQuery(param.getDemandId()).field(SHOPPING_LIST_ID).field(SHOPPING_LIST_ID_BACK).analyzer(WHITESPACE));
    }
    if (StringUtils.isNotBlank(param.getDemandType())) {
      queryBuilder.filter(QueryBuilders.termQuery(INQUIRY_TYPE,param.getDemandType()));
    }
    if (StringUtils.isNotBlank(param.getGarageId())) {
      queryBuilder.filter(QueryBuilders.termQuery(GARAGE_COMPANY_ID,param.getGarageId()));
    }
    if (param.getStartDateStr() != null) {
      queryBuilder.filter(QueryBuilders.rangeQuery(CREATED_STAMP).gt(param.getStartDateStr().getTime()));
    }
    if (param.getEndDateStr() != null) {
      queryBuilder.filter(QueryBuilders.rangeQuery(CREATED_STAMP).lt(param.getEndDateStr().getTime()));
    }
    if (StringUtils.isNotBlank(param.getStatusId())) {
      queryBuilder.filter(QueryBuilders.termQuery(STATUS_ID,param.getStatusId()));
    }
    if (null != param.getOrdered() && param.getOrdered()) {
      queryBuilder.filter(QueryBuilders.existsQuery(ORDER_IDS));
    }
    if (StringUtils.isNotBlank(param.getCarBrandId())) {
      queryBuilder.filter(QueryBuilders.termQuery(CAR_BRAND_ID,param.getCarBrandId()));
    }
    if (StringUtils.isNotBlank(param.getSourceId())) {
      queryBuilder.filter(QueryBuilders.termQuery(SOURCE_ID,param.getSourceId()));
    }
    if (StringUtils.isNotBlank(param.getVin())) {
      queryBuilder.filter(QueryBuilders.termQuery(VIN,param.getVin()));
    }
    if (StringUtils.isNotBlank(param.getProvinceId()) && !ALL_PROVINCE_ID.equals(param.getProvinceId().trim())) {
      queryBuilder.filter(QueryBuilders.termQuery(PROVINCE_GEO_ID,param.getProvinceId()));
    }
    if (StringUtils.isNotBlank(param.getCityId()) && !ALL_CITY_ID.equals(param.getCityId().trim())) {
      queryBuilder.filter(QueryBuilders.termQuery(CITY_GEO_ID,param.getCityId()));
    }
    if(CollectionUtils.isNotEmpty(param.getCountyIds())){
      queryBuilder.filter(QueryBuilders.termsQuery(COUNTY_GEO_ID,param.getCountyIds()));
    }
    queryBuilder.filter(QueryBuilders.termQuery(ES_DATA_SOURCE_COLUMN, "HOT"));
    return queryBuilder;
  }



  public static  String convertQueryParamToInquiryListPage(InquirySearchConditionParam param,int page,int size) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(convertInquiryQueryParamToQueryBuilder(param))
        .from((page - 1) * size)
        .size(size);

    searchSourceBuilder.sort(CREATED_STAMP, SortOrder.DESC);
    return searchSourceBuilder.toString();
  }

  public static  String convertQueryParamToInquiryListPage(InquirySearchPcConditionParam param,int page,int size) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(convertQueryParamToQueryBuilder(param))
        .from((page - 1) * size)
        .size(size);

    searchSourceBuilder.sort(CREATED_STAMP, SortOrder.DESC);
    return searchSourceBuilder.toString();
  }

  public static String convertQueryParamToQuoteSupplierHeaderListPage(QuoteSupplierHeaderSearchParam param,int page,int size) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(convertQuoteSupplierHeaderQueryParamToQueryBuilder(param))
        .from((page - 1) * size)
        .size(size);

    searchSourceBuilder.sort(CREATED_STAMP, getSortOrder(param.getOrder()));
    return searchSourceBuilder.toString();
  }

  public static String getIdsQueryString(List<Long> ids) {
    if (CollectionUtils.isEmpty(ids)) {
      return "";
    }
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQueryBuilder.filter(QueryBuilders.termsQuery("id", ids));
    searchSourceBuilder.query(boolQueryBuilder);
    return searchSourceBuilder.toString();
  }

  public static String getIds(List<String> inquiryIds,List<Long> quoteSupplierHeaderIds) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQueryBuilder.must(QueryBuilders.termsQuery(INQUIRY_ID, inquiryIds));
    boolQueryBuilder.mustNot(QueryBuilders.termsQuery("id", quoteSupplierHeaderIds));
    searchSourceBuilder.query(boolQueryBuilder);
    return searchSourceBuilder.toString();
  }



  private static SortOrder getSortOrder(String str) {
    if ("asc".equalsIgnoreCase(str)) {
      return SortOrder.ASC;
    }else{
      return SortOrder.DESC;
    }
  }



  public static InquirySearchDTO convertJsonResponseToInquiryListPage(String jsonResponse) {
    InquirySearchDTO inquirySearchDTO = new InquirySearchDTO();
    List<InquirySearchBaseDTO> res = Lists.newArrayList();
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      JSONArray hitsList = hits.getJSONArray(HITS);
      inquirySearchDTO.setTotal(hits.getInteger(TOTAL));
      if (hitsList != null && !hitsList.isEmpty()) {
        for (int i = 0; i < hitsList.size(); i++) {
          JSONObject hit = hitsList.getJSONObject(i);
          JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
          InquirySearchBaseDTO inquirySearchBaseDTO = new InquirySearchBaseDTO();
          inquirySearchBaseDTO.setCreatedStamp(response.getDate(CREATED_STAMP));
          inquirySearchBaseDTO.setInquiryId(response.getString(SHOPPING_LIST_ID));
          inquirySearchBaseDTO.setStatusId(response.getString(STATUS_ID));
          inquirySearchBaseDTO.setInquiryDataType(response.getObject(ES_DATA_SOURCE_COLUMN,
              InquiryDataTypeEnum.class));
          inquirySearchBaseDTO.setInquiryType(response.getString(INQUIRY_TYPE));
          res.add(inquirySearchBaseDTO);
        }
      }
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
    }
    inquirySearchDTO.setInquiryList(res);
    return inquirySearchDTO;
  }

  public static List<EsInquiryBase> convertJsonResponseToEsInquiryBase(String jsonResponse) {
    List<EsInquiryBase> esInquiryBases = Lists.newArrayList();
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      JSONArray hitsList = hits.getJSONArray(HITS);
      if (hitsList != null && !hitsList.isEmpty()) {
        for (int i = 0; i < hitsList.size(); i++) {
          JSONObject hit = hitsList.getJSONObject(i);
          JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
          EsInquiryBase esInquiryBase = new EsInquiryBase();
          esInquiryBase.setShoppingListId(response.getString(SHOPPING_LIST_ID));
          esInquiryBase.setVin(response.getString(VIN));
          esInquiryBase.setCarBrandId(response.getString(CAR_BRAND_ID));
          if (StringUtils.isNotBlank(response.getString(NEEDS_NAMES_ZH))) {
            esInquiryBase.setNeedsNamesZh(response.getJSONArray(NEEDS_NAMES_ZH).toJavaList(String.class));
          }
          if (StringUtils.isNotBlank(response.getString(NEEDS_NAMES_EN))) {
            esInquiryBase.setNeedsNamesZh(response.getJSONArray(NEEDS_NAMES_EN).toJavaList(String.class));
          }
          if (StringUtils.isNotBlank(response.getString(ORDER_IDS))) {
            esInquiryBase.setOrderIds(response.getJSONArray(ORDER_IDS).toJavaList(String.class));
          }
          esInquiryBase.setInquiryType(response.getString(INQUIRY_TYPE));
          esInquiryBase.setStatusId(response.getString(STATUS_ID));
          esInquiryBase.setCreatedBy(response.getString(CREATED_BY));
          esInquiryBase.setCreatedStamp(response.getDate(CREATED_STAMP));
          esInquiryBase.setDataSource(response.getString(ES_DATA_SOURCE_COLUMN));
          esInquiryBase.setGarageCompanyId(response.getString(GARAGE_COMPANY_ID));
          esInquiryBase.setSourceId(response.getString(SOURCE_ID));
          esInquiryBase.setProvinceGeoId(response.getString(PROVINCE_GEO_ID));
          esInquiryBase.setCityGeoId(response.getString(CITY_GEO_ID));
          esInquiryBase.setCountyGeoId(response.getString(COUNTY_GEO_ID));
          esInquiryBase.setVillageGeoId(response.getString(VILLAGE_GEO_ID));
          esInquiryBases.add(esInquiryBase);
        }
      }
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
    }
    return esInquiryBases;
  }

  public static DemandSearchDTO convertJsonResponseToEsDemandIds(String jsonResponse) {
    List<String> demandIds = Lists.newArrayList();
    DemandSearchDTO demandSearch = new DemandSearchDTO();
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      JSONArray hitsList = hits.getJSONArray(HITS);
      demandSearch.setTotalElements(hits.getInteger(TOTAL));
      if (null != hitsList && !hitsList.isEmpty()) {
        hitsList.forEach(p -> {
          JSONObject hit = (JSONObject)JSON.toJSON(p);
          JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
          demandIds.add(response.getString(SHOPPING_LIST_ID));
        });
      }
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
    }
    demandSearch.setDemandIds(demandIds);
    return demandSearch;
  }


  public static QuoteSupplierHeaderSearchDTO convertJsonResponseToQuoteSupplierHeaderListPage(String jsonResponse) {
    QuoteSupplierHeaderSearchDTO quoteSupplierHeaderSearch = new QuoteSupplierHeaderSearchDTO();
    List<QuoteSupplierHeaderSearchBaseDTO> res = Lists.newArrayList();
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      JSONArray hitsList = hits.getJSONArray(HITS);
      quoteSupplierHeaderSearch.setTotal(hits.getInteger(TOTAL));
      if (hitsList != null && !hitsList.isEmpty()) {
        for (int i = 0; i < hitsList.size(); i++) {
          JSONObject hit = hitsList.getJSONObject(i);
          JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
          QuoteSupplierHeaderSearchBaseDTO quoteSupplierHeaderSearchBaseDTO = new QuoteSupplierHeaderSearchBaseDTO();
          quoteSupplierHeaderSearchBaseDTO.setCreatedStamp(response.getDate(CREATED_STAMP));
          quoteSupplierHeaderSearchBaseDTO.setQuoteSupplierHeaderId(response.getString("id"));
          quoteSupplierHeaderSearchBaseDTO.setDataType(response.getObject(ES_DATA_SOURCE_COLUMN,
              DataTypeEnum.class));

          res.add(quoteSupplierHeaderSearchBaseDTO);
        }
      }
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
    }
    quoteSupplierHeaderSearch.setQuoteSupplierHeaders(res);
    return quoteSupplierHeaderSearch;
  }



  public static String convertQueryParamToInquiryList(List<String> inquiryIds) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.filter(QueryBuilders.termsQuery(SHOPPING_LIST_ID, inquiryIds));
    queryBuilder.filter(QueryBuilders.termQuery(ES_DATA_SOURCE_COLUMN, "COLD"));
    searchSourceBuilder.query(queryBuilder);
    searchSourceBuilder.size(1000000);
    return searchSourceBuilder.toString();
  }

  public static List<String> convertJsonResponseToInquiryIds(String jsonResponse) {
    List<String> res = Lists.newArrayList();
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      JSONArray hitsList = hits.getJSONArray(HITS);
      if (hitsList != null && !hitsList.isEmpty()) {
        for (int i = 0; i < hitsList.size(); i++) {
          JSONObject hit = hitsList.getJSONObject(i);
          JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
          res.add(response.getString(SHOPPING_LIST_ID));
        }
      }
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
    }
    return res;
  }

  public static String convertQueryParamToQuoteSupplierList(List<String> inquiryIds) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.filter(QueryBuilders.termQuery(ES_DATA_SOURCE_COLUMN, "COLD"));
    queryBuilder.filter(QueryBuilders.termsQuery(INQUIRY_ID, inquiryIds));
    searchSourceBuilder.query(queryBuilder);
    searchSourceBuilder.size(1000000);
    return searchSourceBuilder.toString();
  }


  public static Map<String,List<Long>> convertJsonResponseToInquiryIdQuoteSupplierHeaderIdsMap(String jsonResponse) {
    List<QuoteSupplierHeaderSearchBaseDTO> res = Lists.newArrayList();
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      JSONArray hitsList = hits.getJSONArray(HITS);
      if (hitsList != null && !hitsList.isEmpty()) {
        for (int i = 0; i < hitsList.size(); i++) {
          JSONObject hit = hitsList.getJSONObject(i);
          JSONObject response = hit.getJSONObject(ES_SOURCE_COLUMN);
          QuoteSupplierHeaderSearchBaseDTO quoteSupplierHeaderSearchBaseDTO = new QuoteSupplierHeaderSearchBaseDTO();
          quoteSupplierHeaderSearchBaseDTO.setInquiryId(response.getString(INQUIRY_ID));
          quoteSupplierHeaderSearchBaseDTO.setQuoteSupplierHeaderId(response.getString("id"));
          res.add(quoteSupplierHeaderSearchBaseDTO);
        }
      }
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
    }
    Map<String, List<Long>> idMap = Maps.newHashMap();
    for (QuoteSupplierHeaderSearchBaseDTO quoteSupplierHeaderSearch : res) {
      if (idMap.containsKey(quoteSupplierHeaderSearch.getInquiryId())) {
        List<Long> quoteSupplierHeaderIds = idMap.get(quoteSupplierHeaderSearch.getInquiryId());
        quoteSupplierHeaderIds.add(Long.valueOf(quoteSupplierHeaderSearch.getQuoteSupplierHeaderId()));
        idMap.put(quoteSupplierHeaderSearch.getInquiryId(), quoteSupplierHeaderIds);
      }else{
        List<Long> quoteSupplierHeaderIds = Lists.newArrayList();
        quoteSupplierHeaderIds.add(Long.valueOf(quoteSupplierHeaderSearch.getQuoteSupplierHeaderId()));
        idMap.put(quoteSupplierHeaderSearch.getInquiryId(), quoteSupplierHeaderIds);
      }
    }
    return idMap;
  }

  public static String getQueryCloseAndReturnByPageParam(Long startStamp, Long endStamp) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.filter(QueryBuilders.termsQuery(STATUS_ID, Lists.newArrayList("IS_CLOSED","RETURNED")));
    queryBuilder.filter(
        QueryBuilders.rangeQuery(CREATED_STAMP).gte(startStamp)
            .boost(0f));
    queryBuilder.filter(
        QueryBuilders.rangeQuery(CREATED_STAMP).lt(endStamp)
            .boost(0f));
    searchSourceBuilder.query(queryBuilder);
    searchSourceBuilder.size(1000000);

    return searchSourceBuilder.toString();
  }

  public static String getEsInquiryBaseByInquiryIdsParamString(List<String> inquiryIds) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.filter(QueryBuilders.termsQuery(SHOPPING_LIST_ID, inquiryIds));
    searchSourceBuilder.query(queryBuilder);
    return searchSourceBuilder.toString();
  }

  public static String getDemandPageByTrackParam(DemandSearchAdminParam param, int page, int size) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.query(convertTrackParamToQueryBuilder(param))
        .from((page - 1) * size)
        .size(size);
    searchSourceBuilder.sort(CREATED_STAMP, SortOrder.DESC);
    return searchSourceBuilder.toString();
  }

  public static String getInquiryCountByUserIdParamString(String userId) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
    queryBuilder.filter(QueryBuilders.termQuery(CREATED_BY, userId));
    queryBuilder.mustNot(QueryBuilders.termQuery(STATUS_ID, "DRAFT"));
    searchSourceBuilder.query(queryBuilder);
    return searchSourceBuilder.toString();
  }

  public static Integer convertJsonResponseToTotalInteger(String jsonResponse) {
    try {
      JSONObject object = JSON.parseObject(jsonResponse);
      JSONObject hits = object.getJSONObject(HITS);
      return hits.getInteger(TOTAL);
    } catch (Exception e) {
      logger.error(ES_ERROR_MESSAGE, e);
      return 0;
    }
  }
}
