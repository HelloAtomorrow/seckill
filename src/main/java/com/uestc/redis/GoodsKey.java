package com.uestc.redis;

public class GoodsKey extends BasePrefix {

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey goodslistKey = new GoodsKey(60, "goodsListKey");

    public static GoodsKey getSeckillStock = new GoodsKey(0, "getSeckillStock");

    public static GoodsKey isGoodsOver = new GoodsKey(0, "isGoodsOver");
}
