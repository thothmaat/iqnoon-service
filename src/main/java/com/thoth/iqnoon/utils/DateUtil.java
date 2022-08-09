package com.thoth.iqnoon.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil {

  private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

  private DateUtil() {

  }

  public static Date stringToDate(String dateStr) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return simpleDateFormat.parse(dateStr);
    } catch (ParseException e) {
      logger.warn("时间格式转化错误", e);
    }
    return new Date();
  }

  public static Date longToDate(Long timeStamp) {
    try {
      return new Date(timeStamp);
    } catch (Exception e) {
      logger.warn("时间格式转化错误",e);
    }
    return new Date();
  }

  public static String getTodayDate() {
    LocalDateTime today = LocalDateTime.now();
    long stamp = today.toEpochSecond(ZoneOffset.of("+8"));
    return dateToStr(new Date(stamp * 1000));
  }

  public static String getPreDayDate(int days) {
    if (days < 0) {
      return getTodayDate();
    }
    LocalDateTime preDay = LocalDateTime
        .of(LocalDate.now().plusDays(-days), LocalTime.MIN);
    long preStamp = preDay.toEpochSecond(ZoneOffset.of("+8"));
    return dateToStr(new Date(preStamp * 1000));
  }

  private static String dateToStr(Date date) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return simpleDateFormat.format(date);
  }
}
