package com.uestc.controller;

import com.uestc.domain.OrderInfo;
import com.uestc.domain.SeckillUser;
import com.uestc.result.CodeMsg;
import com.uestc.result.Result;
import com.uestc.service.GoodsService;
import com.uestc.service.OrderService;
import com.uestc.vo.GoodsVo;
import com.uestc.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(SeckillUser seckillUser,
                                      @RequestParam("orderId")long orderId) {
        if (seckillUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setGoodsVo(goodsVo);
        orderDetailVo.setOrderInfo(order);
        return Result.success(orderDetailVo);
    }
}
