package com.uestc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    /**
     * 用正则表达式验证是否为手机号
     * @param src
     * @return
     */
    public static boolean isMobile(String src) {
        if (src.isEmpty()) {
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(src);
        return matcher.matches();
    }
}
