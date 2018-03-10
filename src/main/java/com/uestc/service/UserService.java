package com.uestc.service;

import com.uestc.dao.UserDao;
import com.uestc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Transactional//这个注解可以开启事务，在这里也可以不加事务。
    public User getById(int id) {
        return userDao.getById(id);
    }
}
