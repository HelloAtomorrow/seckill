package com.uestc.rabbitmq;

import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillOrder;
import com.uestc.domain.SeckillUser;
import com.uestc.redis.RedisService;
import com.uestc.result.CodeMsg;
import com.uestc.result.Result;
import com.uestc.service.GoodsService;
import com.uestc.service.OrderService;
import com.uestc.service.SeckillService;
import com.uestc.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

//    @RabbitListener(queues = MQConfig.DIRECT_QUEUE)
//    public void receive(String message) {
//        System.out.println("receive message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String message) {
//        System.out.println("topic1 receive message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String message) {
//        System.out.println("topic2 receive message: " + message);
//    }
//
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveheaders(byte[] message) {
//        System.out.println("headers receive message: " + new String(message));
//    }

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receiveSeckillMessage(String message) {
        SeckillMessage seckillMessage = redisService.stringToBean(message, SeckillMessage.class);
        SeckillUser seckillUser = seckillMessage.getSeckillUser();
        long goodsId = seckillMessage.getGoodsId();

        //判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            return;
        }
        //判断是否已经秒杀过了，拒绝重复秒杀
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);
        if (seckillOrder != null) {
            return;
        }
        seckillService.seckill(seckillUser, goodsVo);
    }
}
