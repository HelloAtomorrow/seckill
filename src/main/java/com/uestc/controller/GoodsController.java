package com.uestc.controller;

import com.uestc.domain.Goods;
import com.uestc.domain.SeckillUser;
import com.uestc.service.GoodsService;
import com.uestc.service.SeckillUserService;
import com.uestc.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String toList(Model model, SeckillUser seckillUser) {
        model.addAttribute("user", seckillUser);
        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVos);
        return "goods_list";
    }

    /**
     *
     * @param model
     * @param seckillUser
     * @param goodsId 实际生产中很少使用goodsId这种自增的id，容易被别人遍历。业内常用snowflake
     * @return
     */
    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model, SeckillUser seckillUser,
                           @PathVariable("goodsId")long goodsId) {
        model.addAttribute("user", seckillUser);
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);

        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long nowTime = System.currentTimeMillis();
        int seckillStatu = 0;
        long remainSeconds = 0;

        if (nowTime < startTime) {
            seckillStatu = 0;
            remainSeconds = (startTime - nowTime) / 1000;
        } else if (nowTime > endTime) {
            seckillStatu = 2;
            remainSeconds = -1;
        } else {
            seckillStatu = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatu", seckillStatu);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
}
