package com.uestc.controller;

import com.uestc.domain.SeckillUser;
import com.uestc.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillUserService seckillUserService;

    @RequestMapping("/to_list")
    public String toList(Model model,
//                         @CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN, required = false)String cookieToken,
//                         @RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN, required = false)String paramToken) {
                         SeckillUser seckillUser) {
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        SeckillUser seckillUser = seckillUserService.getByToken(response, token);
//        System.out.println(seckillUser);
        model.addAttribute("user", seckillUser);
        return "goods_list";
    }
}
