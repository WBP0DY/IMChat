package com.wbp.mallchat.common.websocket.service.adapter;

import cn.hutool.core.bean.BeanUtil;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

public class UserAdapter {
    public static User buildUserSave(String openID) {
        return User.builder().openId(openID).build();
    }

    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        User user= new User();
        user.setId(id);
        user.setName(userInfo.getNickname());
        user.setAvatar(userInfo.getHeadImgUrl());
        return user;
    }

    public static UserInfoResp buildUserInfo(User user, Integer nameChangeCount) {
        UserInfoResp vo = new UserInfoResp();
        BeanUtil.copyProperties(user, vo);
        vo.setModifyNameChange(nameChangeCount);
        return vo;
    }

    public static User buildModifyName(Long uid, String name) {
        return User.builder().id(uid).name(name).build();
    }
}
