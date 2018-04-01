package com.uestc.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取单个对象
     * @param prefix：key的前缀和生存时间
     * @param key
     * @param clazz：对象的类型
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T>clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //添加前缀生成真正的key
            String realKey = prefix.getPrefix() + key;
            String string = jedis.get(realKey);
            T t = stringToBean(string, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     * @param prefix：key的前缀和生存时间
     * @param key
     * @param value：需要set的对象
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String string = beanToString(value);
            if (string == null || string.length() <= 0) {
                return false;
            }
            //添加前缀生成真正的key
            String realKey = prefix.getPrefix() + key;
            //设置过期时间
            int seconds = prefix.expireSeconds();
            if (seconds == 0) {
                jedis.set(realKey, string);
            } else {
                jedis.setex(realKey, seconds, string);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //添加前缀生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加数值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //添加前缀生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少数值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //添加前缀生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 把对象转换成字符串
     * @param value
     * @param <T>
     * @return
     */
    public <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将字符串转换成对象
     * @param string
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T stringToBean(String string, Class<T> clazz) {
        if (string == null || string.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(string);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(string);
        } else if (clazz == String.class) {
            return (T) string;
        } else {
            return JSON.toJavaObject(JSON.parseObject(string), clazz);
        }
    }

    /**
     * 将用完的jedis连接返回连接池
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
