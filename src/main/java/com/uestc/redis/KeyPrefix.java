package com.uestc.redis;

public interface KeyPrefix {

    /**
     * key的生存时间
     * @return
     */
    public int expireSeconds();

    /**
     * key的前缀信息，用于区分不同类型的key
     * @return
     */
    public String getPrefix();
}
