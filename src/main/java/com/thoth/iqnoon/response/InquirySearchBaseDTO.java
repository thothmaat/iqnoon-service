package com.thoth.iqnoon.response;

import com.casstime.ec.cloud.inquiry.es.spi.enumeration.InquiryDataTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author: 毕巍巍
 * @Date: 2019/10/23 18:06
 * @Description:
 */
@Data
public class InquirySearchBaseDTO {

  private String inquiryId;

  private InquiryDataTypeEnum inquiryDataType;

  private String inquiryType;

  private String statusId;

  private Date createdStamp;
}
