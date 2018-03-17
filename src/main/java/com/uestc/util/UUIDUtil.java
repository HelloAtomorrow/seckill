package com.uestc.util;

import java.util.UUID;

//用于生成token
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
