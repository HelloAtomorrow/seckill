package com.uestc.service;

import com.uestc.dao.GoodsDao;
import com.uestc.domain.Goods;
import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillOrder;
import com.uestc.domain.SeckillUser;
import com.uestc.redis.GoodsKey;
import com.uestc.redis.RedisService;
import com.uestc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional //标识这是一个事务
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goodsVo) {
        //减少库存
        boolean isSuccess = goodsService.reduceStock(goodsVo);
        if (isSuccess) {
            //下订单
            return orderService.createOrder(seckillUser, goodsVo);
        } else {
            setGoodsOver(goodsVo.getId());
            return null;
        }
    }

    /**
     * 获得秒杀结果
     * 如果查到订单信息，则秒杀成功，
     * 如果redis缓存中已经存在秒杀结束的key了，则秒杀失败，
     * 否则，继续排队。
     * @param userId
     * @param goodsId
     * @return
     */
    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(GoodsKey.isGoodsOver, "" + goodsId);
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(GoodsKey.isGoodsOver, "" + goodsId,true);
    }
}
