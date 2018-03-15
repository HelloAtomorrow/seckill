package com.uestc.service;

import com.uestc.dao.SeckillUserDao;
import com.uestc.domain.SeckillUser;
import com.uestc.exception.GlobleException;
import com.uestc.result.CodeMsg;
import com.uestc.util.MD5Util;
import com.uestc.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillUserService {

    @Autowired
    SeckillUserDao seckillUserDao;

    public SeckillUser getById(Long id) {
        return seckillUserDao.getById(id);
    }

    public boolean login(LoginVo loginVo) {
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
        return true;
    }
}