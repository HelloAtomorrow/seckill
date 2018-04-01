package com.uestc.controller;

import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillOrder;
import com.uestc.domain.SeckillUser;
import com.uestc.rabbitmq.MQSender;
import com.uestc.rabbitmq.SeckillMessage;
import com.uestc.redis.GoodsKey;
import com.uestc.redis.RedisService;
import com.uestc.result.CodeMsg;
import com.uestc.result.Result;
import com.uestc.service.GoodsService;
import com.uestc.service.OrderService;
import com.uestc.service.SeckillService;
import com.uestc.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVoList) {
            localOverMap.put(goodsVo.getId(), false);
            redisService.set(GoodsKey.getSeckillStock, "" + goodsVo.getId(), goodsVo.getStockCount());
        }
    }

    @RequestMapping(value="do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> seckill(Model model, SeckillUser seckillUser, @RequestParam("goodsId")long goodsId) {
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //内存标记
        boolean isOver = localOverMap.get(goodsId);
        if (isOver) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //预减库存
        long stock = redisService.decr(GoodsKey.getSeckillStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀过了，拒绝重复秒杀
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);
        if (seckillOrder != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //加入消息队列
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setSeckillUser(seckillUser);
        seckillMessage.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(seckillMessage);
        return Result.success(0);
        /*
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        //判断库存
        int stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //判断是否已经秒杀过了，拒绝重复秒杀
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);
        if (seckillOrder != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //减库存 下订单 写入秒杀订单（必须使用事务，这三步必须同时成功）
        OrderInfo orderInfo = seckillService.seckill(seckillUser, goodsVo);
        return Result.success(orderInfo);
        */
    }

    /**
     * 返回秒杀结果
     * @param model
     * @param seckillUser
     * @param goodsId
     * @return orderId表示成功，
     *         0表示排队中，继续轮询，
     *         -1表示秒杀失败。
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, SeckillUser seckillUser,
                                         @RequestParam("goodsId")long goodsId) {
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(seckillUser.getId(), goodsId);
        return Result.success(result);
    }
}