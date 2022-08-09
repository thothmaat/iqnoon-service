package com.thoth.iqnoon.request;

import com.casstime.ec.cloud.inquiry.es.spi.jackson.SearchConditionDeSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: 毕巍巍
 * @Date: 2019/10/24 16:35
 * @Description:
 */
@Data
public class InquirySearchConditionParam {
  @JsonDeserialize(using = SearchConditionDeSerialize.class)
  private String searchContext;
  private Date createdStartDate;
  private Date createdEndDate;
  private List<String> statusIds;
  private List<String> carBrandIds;
  private CommonRoleCondition commonRoleCondition;
  private List<String> adminCreateBys;
  private List<String> adminCompanyIds;
  @Data
  public static class CommonRoleCondition{
    private String garageCompanyId;
    private List<String> createdBys;
  }
}
