package com.thoth.iqnoon.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: dengpeng chen
 * @date: 2019/10/24 16 : 15
 * @description:
 */
public class NeedNameUtil {

  private static final String ZH_REG = "[^\u4e00-\u9fa5]";
  private static final String EN_REG = "[^a-zA-Z0-9]";
  private NeedNameUtil() {

  }

  public static List<String> matchZhStr(List<String> needsNames) {
    return matchStr(ZH_REG, needsNames);
  }

  public static List<String> matchEnStr(List<String> needsNames) {
    return matchStr(EN_REG, needsNames);
  }

  private static List<String> matchStr(String reg, List<String> needsNames) {
    if (null == needsNames) {
      return Collections.emptyList();
    }

    List<String> result = Lists.newArrayList();
    for (String needsName: needsNames) {
      if (StringUtils.isBlank(needsName)) {
        continue;
      }
      if (StringUtils.isNotBlank(needsName.replaceAll(reg, ""))) {
        result.add(needsName.replaceAll(reg, ""));
      }
    }

    return result.stream().distinct().collect(Collectors.toList());
  }
}
