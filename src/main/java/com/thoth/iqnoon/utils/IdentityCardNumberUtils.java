package com.thoth.iqnoon.utils;


import com.thoth.iqnoon.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.util.*;

public class IdentityCardNumberUtils {

    /*省份数据*/
    private static final Map<Integer, String> PROVINCES = new HashMap<>(36);
    static {
        PROVINCES.put(11, "北京");
        PROVINCES.put(12, "天津");
        PROVINCES.put(13, "河北");
        PROVINCES.put(14, "山西");
        PROVINCES.put(15, "内蒙古");
        PROVINCES.put(21, "辽宁");
        PROVINCES.put(22, "吉林");
        PROVINCES.put(23, "黑龙江");
        PROVINCES.put(31, "上海");
        PROVINCES.put(32, "江苏");
        PROVINCES.put(33, "浙江");
        PROVINCES.put(34, "安徽");
        PROVINCES.put(35, "福建");
        PROVINCES.put(36, "江西");
        PROVINCES.put(37, "山东");
        PROVINCES.put(41, "河南");
        PROVINCES.put(42, "湖北 ");
        PROVINCES.put(43, "湖南");
        PROVINCES.put(44, "广东");
        PROVINCES.put(45, "广西");
        PROVINCES.put(46, "海南");
        PROVINCES.put(50, "重庆");
        PROVINCES.put(51, "四川");
        PROVINCES.put(52, "贵州");
        PROVINCES.put(53, "云南");
        PROVINCES.put(54, "西藏 ");
        PROVINCES.put(61, "陕西");
        PROVINCES.put(62, "甘肃");
        PROVINCES.put(63, "青海");
        PROVINCES.put(64, "宁夏");
        PROVINCES.put(65, "新疆");
        PROVINCES.put(71, "台湾");
        PROVINCES.put(81, "香港");
        PROVINCES.put(82, "澳门");
    }

    /**
     * @description 验证身份号前18，符合一个简单的身份证号，年是18-20开头的年份
     * @param identityCardNumber
     * @return
     */
    private static boolean checkCode(String identityCardNumber) {
        String regex = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[\\dXx]$";
        if (identityCardNumber.matches(regex)) {
            //校验合法
            int[] factor = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
            String[] parity = new String[] { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
            String code = identityCardNumber.substring(17);
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += Integer.parseInt(String.valueOf(identityCardNumber.charAt(i))) * factor[i];
            }
            if (parity[sum % 11].equals(code.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
    private boolean cardCodeVerifySimple(String idNumber) {
        //第一代身份证正则表达式(15位)
        String isIDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        //第二代身份证正则表达式(18位)
        String isIDCard2 ="^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z])$";
        //验证身份证
        if (idNumber.matches(isIDCard1) || idNumber.matches(isIDCard2)) {
            return true;
        }
        return false;
    }
    /**
     * 构造符合身份证号规律的身份证号
     * @param identityCardNumber
     * @return
     */
    private static String generateIdNumber(String identityCardNumber) {
        String regex = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[\\dXx]$";
        if (identityCardNumber.matches(regex)) {
            int[] factor = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
            String[] parity = new String[] { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
            String code = identityCardNumber.substring(17);
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += Integer.parseInt(String.valueOf(identityCardNumber.charAt(i))) * factor[i];
            }
            return identityCardNumber.substring(0,17) + parity[sum % 11];
        }
        return identityCardNumber;
    }
    // 组合起来，分段验证，提高效率(符合正则使用习惯，分段验证，提高性能)
    public static boolean validateIdCard(String idCardNumber) {
        if (checkCode(idCardNumber)) {
            String date = idCardNumber.substring(6, 14);
            if (checkMonth(date)) {
                if (checkProvince(idCardNumber.substring(0, 2))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证年份
     * @param year
     * @return
     */
    private static boolean checkMonth(String year) {
        String pattern = "^(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)$";
        if (year.matches(pattern)) {
            String month = year.substring(4, 6);
            try{
                Date date = DateUtils.parseDateStrictly(year,"yyyyMMdd");
                //这里检验月的原因的，润年校验
                if (date.getMonth() == (Integer.parseInt(month) - 1)) {
                    return true;
                }
            }catch (Exception e){

            }
        }
        return false;
    }

    /**
     * 检查省份
     * @param province
     * @return
     */
    private static boolean checkProvince(String province) {
        String pattern = "^[1-9]\\d";
        if (province.matches(pattern)) {
            return PROVINCES.containsKey(Integer.parseInt(province));
        }
        return false;
    }



    /**
     * 获取出生日期
     *
     * @return 返回字符串类型
     */
    public String getBirthFromIdCard(String idCard) {
        if (idCard.length() != 18 && idCard.length() != 15) {
            return "请输入正确的身份证号码";
        }
        if (idCard.length() == 18) {
            String year = idCard.substring(6).substring(0, 4);// 得到年份
            String month = idCard.substring(10).substring(0, 2);// 得到月份
            String day = idCard.substring(12).substring(0, 2);// 得到日
            return (year + Constants.SHORT_LINE + month + Constants.SHORT_LINE + day);
        } else {
            String year = "19" + idCard.substring(6, 8);// 年份
            String month = idCard.substring(8, 10);// 月份
            String day = idCard.substring(10, 12);// 得到日
            return (year + Constants.SHORT_LINE + month + Constants.SHORT_LINE + day);
        }
    }

    /**
     * 获取出生日期
     * @return 返回日期格式
     */
    public static LocalDate getBirthDayFromIdCard(String idCard) {
        LocalDate birthday = null;
        if (idCard.length() == 18) {
            String year = idCard.substring(6).substring(0, 4);// 得到年份
            String month = idCard.substring(10).substring(0, 2);// 得到月份
            String day = idCard.substring(12).substring(0, 2);// 得到日
            birthday = LocalDate.parse(year + Constants.SHORT_LINE + month + Constants.SHORT_LINE + day);
        } else if (idCard.length() == 15) {
            String year = "19" + idCard.substring(6, 8);// 年份
            String month = idCard.substring(8, 10);// 月份
            String day = idCard.substring(10, 12);// 得到日
            birthday = LocalDate.parse(year + Constants.SHORT_LINE + month + Constants.SHORT_LINE + day);
        }
        return birthday;
    }

    /**
     * 获取性别 0=未知的性别,9=未说明的性别,2=女性,1=男性
     * @return int
     */
    public int getSexFromIdCard(String idCard) {
        int gender = 9;
        // 身份证号码为空
        if (Objects.equals(idCard, StringUtils.EMPTY) || idCard.length() <= 0){
            return 0;
        }
        if (idCard.length() == 18) {
            if (Integer.parseInt(idCard.substring(16).substring(0, 1)) % 2 == 0) {
                gender = 2; // 女
            } else {
                gender = 1; // 男
            }
        } else if (idCard.length() == 15) {
            String genderFlag = idCard.substring(14, 15);// 用户的性别
            if (Integer.parseInt(genderFlag) % 2 == 0) {
                gender = 2; // 女
            } else {
                gender = 1; // 男
            }
        }
        return gender;
    }

    /**
     * 根据身份证的号码算出当前身份证持有者的年龄
     * @return  -1(表示异常) 0 (身份证号码为空)
     */
    public int getAgeFromIdCard(String idCardNumber) {
        try {
            int age = 0;
            if (StringUtils.isEmpty(idCardNumber)) {
                return age;
            }

            String birth = "";
            if (idCardNumber.length() == 18) {
                birth = idCardNumber.substring(6, 14);
            } else if (idCardNumber.length() == 15) {
                birth = "19" + idCardNumber.substring(6, 12);
            }

            int year = Integer.parseInt(birth.substring(0, 4));
            int month = Integer.parseInt(birth.substring(4, 6));
            int day = Integer.parseInt(birth.substring(6));
            Calendar cal = Calendar.getInstance();
            age = cal.get(Calendar.YEAR) - year;
            //周岁计算
            int subMonth = month - 1;
            if (cal.get(Calendar.MONTH) < subMonth || (cal.get(Calendar.MONTH) == subMonth && cal.get(Calendar.DATE) < day)) {
                age--;
            }
            return age;
        } catch (Exception e) {

        }
        return -1;
    }

    /**
     * 15 位身份证号码转 18 位
     * 15位身份证号码：第7、8位为出生年份（两位数），第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
     * 18位身份证号码：第7、8、9、10位为出生年份（四位数），第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
     */
    public StringBuffer IdCardMethod15To18(String idCard) {
        //将字符串转化为buffer进行操作
        StringBuffer stringBuffer = new StringBuffer(idCard);
        //身份证最后一位校验码，X代表10（顺序固定）
        char[] checkIndex = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        //在第6位插入年份的前两位19
        stringBuffer.insert(6, "19");
        for (int i = 0; i < stringBuffer.length(); i++) {
            char c = stringBuffer.charAt(i);
            //前17位数字
            int ai = Integer.parseInt(String.valueOf(c));
            //前17位每位对应的系数（7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 ）
            int wi = ((int) Math.pow(2, stringBuffer.length() - i)) % 11;
            //总和（每位数字乘以系数再相加）
            sum = sum + ai * wi;
        }
        //总和除以11求余
        int indexOf = sum % 11;
        //根据余数作为下表在校验码数组里取值
        stringBuffer.append(checkIndex[indexOf]);
        return stringBuffer;
    }
}
