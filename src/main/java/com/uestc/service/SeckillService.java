package com.uestc.service;

import com.uestc.dao.GoodsDao;
import com.uestc.domain.Goods;
import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillUser;
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

    @Transactional //标识这是一个事务
    public OrderInfo seckill(SeckillUser seckillUser, GoodsVo goodsVo) {
        //减少库存
        goodsService.reduceStock(goodsVo);
        //下订单
        return orderService.createOrder(seckillUser, goodsVo);
    }
}
