package com.uestc.controller;

import com.uestc.domain.User;
import com.uestc.redis.RedisService;
import com.uestc.redis.UserKey;
import com.uestc.result.CodeMsg;
import com.uestc.result.Result;
import com.uestc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "hahaha");
        return "hello";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(Model model) {
        return Result.success("hello json");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError(Model model) {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById,"1", User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("user1");
        boolean flag = redisService.set(UserKey.getById, "1", user);
        return Result.success(flag);
    }
}
