package com.uestc.controller;

import com.uestc.domain.SeckillUser;
import com.uestc.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("info")
    @ResponseBody
    public Result<SeckillUser> info(Model model, SeckillUser seckillUser) {
        return Result.success(seckillUser);
    }
}
