package com.example.express.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IDValidateUtils {

    static String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};//十七位数字本体码权重

    static String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"}; //最后一位的校验码字符值

    public static void main(String[] args) {
        System.out.println(check("41272519910506****"));
    }

    /***
     * 验证身份证
     * @param idStr
     * @return
     */
    public static boolean check(String idStr) {
        try {
            if (null == idStr) {// 验证非空
//                System.out.println("身份证号码不能为空");
                return false;
            }
            if (idStr.length() != 15 && idStr.length() != 18) {// 只能是15位或者18位
//                System.out.println("身份证号码长度只能是15位或者18位");
                return false;
            }

            String Ai = "";
            if (idStr.length() == 18) {
                Ai = idStr.substring(0, 17);
            }

            if (idStr.length() == 15) {//  将15位身份证转换为 17位身份证，最后加上最后一位，转换为18位身份证
                // 15位身份证是没有校验位的，同时采用的是2位数字来表示出生年份，并且 15位的身份证号码确定都是19**年的
                Ai = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
            }
            if (!isNumber(Ai)) { // 验证身份证前17位是否都是数字
//                System.out.println("身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。");
                return false;
            }

            int year = Integer.parseInt(Ai.substring(6, 10));// 出生年份
            int month = Integer.parseInt(Ai.substring(10, 12));// 出生月份
            int day = Integer.parseInt(Ai.substring(12, 14));// 出生日期
            String birthDay = year + "-" + month + "-" + day;

            Date birthdate = null;
            try {// 将出生日期转换为Date类型
                birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthDay);
            } catch (ParseException e) {
                e.printStackTrace();
//                System.out.println("身份证生日无效。");
                return false;
            }
            if (birthdate == null || new Date().before(birthdate)) {
//                System.out.println("身份证生日无效。");
                return false;
            }

            GregorianCalendar gc = new GregorianCalendar();//GregorianCalendar 是 Calendar 的一个具体子类，提供了世界上大多数国家/地区使用的标准日历系统。
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            // 验证生日年份是否在范围之内
            if ((gc.get(Calendar.YEAR) - year) > 150 || (gc.getTime().getTime() - s.parse(birthDay).getTime()) < 0) {
//                System.out.println("身份证生日不在有效范围。");
                return false;
            }

            //验证月份
            if (month > 12 || month <= 0) {
//                System.out.println("身份证号中的月份无效");
                return false;
            }

            //验证日期
            gc.setTime(birthdate);
            boolean mflag = false;
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    mflag = (day >= 1 && day <= 31);
                    break;
                case 2: // 公历的2月非闰年有28天,闰年的2月是29天。
                    if (gc.isLeapYear(gc.get(Calendar.YEAR))) {
                        mflag = (day >= 1 && day <= 29);
                    } else {
                        mflag = (day >= 1 && day <= 28);
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    mflag = (day >= 1 && day <= 30);
                    break;
            }
            if (!mflag) {// 日期不对
//                System.out.println("省份证号中的出生日期不对");
                return false;
            }
            // 验证 开头两位数是否是真实有效的地区编码
            if (cityCodeMap.get(Ai.substring(0, 2)) == null) {
//                System.out.println("身份证地区编码错误。");
                return false;
            }

            int TotalmulAiWi = 0;
            for (int i = 0; i < 17; i++) {//先对前17位数字的权求和 ,使用十七位数字本体码加权求和公式   S = Sum(Ai * Wi)
                TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
            }
            int modValue = TotalmulAiWi % 11;//计算模  Y = mod(S, 11) 用计算出来的结果除以11，这样就会出现
            String strVerifyCode = ValCodeArr[modValue]; // 获取最后一位的校验码字符值
            Ai = Ai + strVerifyCode; // 17位身份证 加上最后以为验证数字   得到18位有效的身份证号

            if (!idStr.toUpperCase().equals(Ai.toUpperCase())) {// 判断传过来的身份证号 和 计算得到的身份证号是否相同
//                System.out.println("身份证号码不对");
                return false;
            }
//            System.out.println("正确");
            return true;
        } catch (Exception e) {
//            System.out.println("验证出错");
            e.printStackTrace();
            return false;
        }


    }

    /***
     * 地区编码
     */
    private static Map<String, String> cityCodeMap = new HashMap<String, String>() {
        {
            this.put("11", "北京");
            this.put("12", "天津");
            this.put("13", "河北");
            this.put("14", "山西");
            this.put("15", "内蒙古");
            this.put("21", "辽宁");
            this.put("22", "吉林");
            this.put("23", "黑龙江");
            this.put("31", "上海");
            this.put("32", "江苏");
            this.put("33", "浙江");
            this.put("34", "安徽");
            this.put("35", "福建");
            this.put("36", "江西");
            this.put("37", "山东");
            this.put("41", "河南");
            this.put("42", "湖北");
            this.put("43", "湖南");
            this.put("44", "广东");
            this.put("45", "广西");
            this.put("46", "海南");
            this.put("50", "重庆");
            this.put("51", "四川");
            this.put("52", "贵州");
            this.put("53", "云南");
            this.put("54", "西藏");
            this.put("61", "陕西");
            this.put("62", "甘肃");
            this.put("63", "青海");
            this.put("64", "宁夏");
            this.put("65", "新疆");
            this.put("71", "台湾");
            this.put("81", "香港");
            this.put("82", "澳门");
            this.put("91", "国外");
        }
    };

    /***
     * 判断str是否为纯数字组成
     * @param str
     * @return
     */
    private static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}