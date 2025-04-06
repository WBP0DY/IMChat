package com.wbp.mallchat.common.user.service;

import com.wbp.mallchat.common.user.domain.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wbp
 * @since 2025-02-24
 */
public interface UserService {

    User getByOpenID(String openID);

    Long saveUser(User user);
}
