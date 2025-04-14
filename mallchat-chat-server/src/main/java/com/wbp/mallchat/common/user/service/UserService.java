package com.wbp.mallchat.common.user.service;

import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);
}
