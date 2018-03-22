package com.uestc.redis;

public class GoodsKey extends BasePrefix {

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey goodslistKey = new GoodsKey(60, "goodsListKey");
}
