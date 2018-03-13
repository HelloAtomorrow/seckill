package com.uestc.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    private static final String SALT = "1a2b3c4d";

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 用户password转化为form password
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String string = "" + SALT.charAt(0) + SALT.charAt(2) + inputPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(string);
    }

    /**
     * form password转换为数据库password
     * @param formPass
     * @param randomSalt
     * @return
     */
    public static String formPassToDBPass(String formPass, String randomSalt) {
        String string = "" + SALT.charAt(0) + SALT.charAt(2) + formPass + SALT.charAt(5) + SALT.charAt(4);
        return md5(string);
    }

    /**
     * 直接将用户密码加密为插入到数据库中的密码
     * @param inputPass
     * @param randomSalt
     * @return
     */
    public static String inputPassToDBPass(String inputPass, String randomSalt) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, randomSalt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass(inputPassToFormPass("123456"), SALT));
        System.out.println(inputPassToDBPass("123456", SALT));
    }
}
