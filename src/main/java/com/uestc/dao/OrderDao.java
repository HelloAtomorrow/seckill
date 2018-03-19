package com.uestc.dao;

import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderDao {

    @Select("SELECT * FROM seckill_order WHERE user_id = #{userId} AND goods_id = #{goodsId}")
    SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userId")long userId, @Param("goodsId")long goodsId);

    @Insert("INSERT INTO order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
            "VALUES (#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "SELECT last_insert_id()")
    long insertOrder(OrderInfo orderInfo);

    @Insert("INSERT INTO seckill_order(user_id, goods_id, order_id) " +
            "VALUES (#{userId}, #{goodsId}, #{orderId})")
    void insertSeckillOrder(SeckillOrder seckillOrder);
}