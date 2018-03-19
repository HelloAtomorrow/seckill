package com.uestc.controller;

import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillOrder;
import com.uestc.domain.SeckillUser;
import com.uestc.result.CodeMsg;
import com.uestc.service.GoodsService;
import com.uestc.service.OrderService;
import com.uestc.service.SeckillService;
import com.uestc.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping("do_seckill")
    public String list(Model model, SeckillUser seckillUser, @RequestParam("goodsId")long goodsId) {
        if (seckillUser == null) {
            return "login";
        }
        model.addAttribute("user", seckillUser);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        //判断库存
        int stockCount = goodsVo.getStockCount();
        if (stockCount <= 0) {
            model.addAttribute("errmsg", CodeMsg.SECKILL_OVER.getMsg());
            return "seckill_fail";
        }
        //判断是否已经秒杀过了，拒绝重复秒杀
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(seckillUser.getId(), goodsId);
        if (seckillOrder != null) {
            model.addAttribute("errmsg", CodeMsg.REPEATE_SECKILL.getMsg());
            return "seckill_fail";
        }
        //减库存 下订单 写入秒杀订单（必须使用事务，这三步必须同时成功）
        OrderInfo orderInfo = seckillService.seckill(seckillUser, goodsVo);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVo);
        return "order_detail";
    }
}
