package com.wbp.mallchat.common.user.dao;

import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/zongzibinbin">abin</a>
 * @since 2025-02-24
 */
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public User getByOpenID(String openID) {
        return lambdaQuery().eq(User::getOpenId, openID).one();
    }
}
