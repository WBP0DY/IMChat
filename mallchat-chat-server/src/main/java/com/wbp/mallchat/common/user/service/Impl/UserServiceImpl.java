package com.wbp.mallchat.common.user.service.Impl;

import com.wbp.mallchat.common.user.dao.UserDao;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public User getByOpenID(String openID) {
        return userDao.getByOpenID(openID);
    }

    @Override
    @Transactional
    public Long saveUser(User user) {
        boolean save = userDao.save(user);
        // todo 用户注册的时间
        return user.getId();
    }
}
