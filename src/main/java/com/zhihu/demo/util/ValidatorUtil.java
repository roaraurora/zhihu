package com.zhihu.demo.util;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    //不含":" 不含"-"
    private static final Pattern pattern1 = Pattern.compile("[\\s\\S]*-[\\s\\S]*");
    private static final Pattern pattern2 = Pattern.compile("[\\s\\S]*:[\\s\\S]*");


    public static boolean isIllegal(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher m1 = pattern1.matcher(src);
        Matcher m2 = pattern2.matcher(src);
        return m1.matches() || m2.matches();
    }

    public static void main(String[] args) {
        System.out.println(isIllegal("123456-78912"));
        System.out.println(isIllegal("1234567891"));
    }
}
