package com.uestc.redis;

public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey(10, "soud");
}
