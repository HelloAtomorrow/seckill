package com.uestc.controller;

import com.uestc.domain.Goods;
import com.uestc.domain.SeckillUser;
import com.uestc.redis.GoodsKey;
import com.uestc.redis.RedisService;
import com.uestc.result.Result;
import com.uestc.service.GoodsService;
import com.uestc.service.SeckillUserService;
import com.uestc.vo.GoodsDetailVo;
import com.uestc.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String toList(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         Model model,
                         SeckillUser seckillUser) {
//        //先从缓存中取
        String html = redisService.get(GoodsKey.goodslistKey, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //手动渲染
        model.addAttribute("user", seckillUser);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        SpringWebContext springWebContext = new SpringWebContext(httpServletRequest,
                httpServletResponse, httpServletRequest.getServletContext(),
                httpServletRequest.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", springWebContext);
        //添加到缓存
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.goodslistKey, "", html);
        }
        return html;
    }

    /**
     *
     * @param model
     * @param seckillUser
     * @param goodsId 实际生产中很少使用goodsId这种自增的id，容易被别人遍历。业内常用snowflake
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(Model model, SeckillUser seckillUser,
                                          @PathVariable("goodsId")long goodsId) {

        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
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

        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setSeckillUser(seckillUser);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setSeckillStatu(seckillStatu);
        return Result.success(goodsDetailVo);
    }
}
