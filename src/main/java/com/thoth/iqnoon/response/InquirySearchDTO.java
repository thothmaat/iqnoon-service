package com.thoth.iqnoon.response;

import lombok.Data;

import java.util.List;

/**
 * @author: 毕巍巍
 * @Date: 2019/11/1 17:08
 * @Description:
 */
@Data
public class InquirySearchDTO {

  private List<InquirySearchBaseDTO> inquiryList;

  private Integer total;
}
