package com.uestc.service;

import com.uestc.dao.SeckillUserDao;
import com.uestc.domain.SeckillUser;
import com.uestc.exception.GlobleException;
import com.uestc.redis.RedisService;
import com.uestc.redis.SeckillUserKey;
import com.uestc.result.CodeMsg;
import com.uestc.util.MD5Util;
import com.uestc.util.UUIDUtil;
import com.uestc.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    SeckillUserDao seckillUserDao;

    @Autowired
    RedisService redisService;

    public SeckillUser getById(Long id) {
        SeckillUser seckillUser = redisService.get(SeckillUserKey.getById, ""+id, SeckillUser.class);
        if (seckillUser != null) {
            return seckillUser;
        }
        seckillUser = seckillUserDao.getById(id);
        if (seckillUser != null) {
            redisService.set(SeckillUserKey.getById, ""+id, seckillUser);
        }
        return seckillUser;
    }

    ///////等后续完善，更改密码的服务。这里要注意，更新之后，需要修改缓存。
    public boolean updatePassword(long id, String newPassword) {
        return false;
    }

    /**
     * 根据token从redis中取出用户
     * @param response
     * @param token
     * @return
     */
    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (token.isEmpty()) {
            return null;
        }
        SeckillUser seckillUser = redisService.get(SeckillUserKey.token, token, SeckillUser.class);
        if (seckillUser != null) {
            addCookie(response, token, seckillUser);
        }
        return seckillUser;
    }

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formpass = loginVo.getPassword();

        //判断手机号是否存在
        SeckillUser seckillUser = getById(Long.parseLong(mobile));
        if (seckillUser == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }

        //验证密码
        String dbPass = seckillUser.getPassword();
        String dbSalt = seckillUser.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formpass, dbSalt);
        if (!calcPass.equals(dbPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }

        String token = UUIDUtil.uuid();
        addCookie(response, token, seckillUser);
        return token;
    }

    /**
     * 以token为键，seckillUser为值，添加到redis中。
     * 根据token生成Cookie，通过浏览器返回。
     * @param response
     * @param token
     * @param seckillUser
     */
    private void addCookie(HttpServletResponse response, String token, SeckillUser seckillUser) {
        //生成cookie
        redisService.set(SeckillUserKey.token, token, seckillUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SeckillUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}