package com.thoth.iqnoon.request;

import com.casstime.ec.cloud.inquiry.es.spi.jackson.SearchConditionDeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: 毕巍巍
 * @Date: 2019/11/14 16:54
 * @Description:
 */
@Data
public class InquirySearchPcConditionParam {
  @JsonDeserialize(using = SearchConditionDeSerialize.class)
  private String inquiryId;
  private List<String> statusIds;
  @JsonDeserialize(using = SearchConditionDeSerialize.class)
  private String vin;
  @JsonDeserialize(using = SearchConditionDeSerialize.class)
  private String userNeed;
  private String carBrandId;
  private Date createdStartDate;
  private Date createdEndDate;
  private List<String> createdBys;
  @JsonDeserialize(using = SearchConditionDeSerialize.class)
  private String orderId;
  private List<String> garageCompanyIds;
  private List<String> adminCreateBys;
}
