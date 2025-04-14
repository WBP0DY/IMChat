package com.wbp.mallchat.common.user.service.Impl;

import com.wbp.mallchat.common.exception.BusinessException;
import com.wbp.mallchat.common.user.dao.ItemConfigDao;
import com.wbp.mallchat.common.user.dao.UserBackpackDao;
import com.wbp.mallchat.common.user.dao.UserDao;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.domain.entity.UserBackpack;
import com.wbp.mallchat.common.user.domain.enums.ItemEnum;
import com.wbp.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.wbp.mallchat.common.user.service.UserService;
import com.wbp.mallchat.common.utils.AssertUtil;
import com.wbp.mallchat.common.websocket.service.adapter.UserAdapter;
import org.aspectj.bridge.MessageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBackpackDao userBackpackDao;

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

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer nameChangeCount = userBackpackDao.getNameChangeCount(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user, nameChangeCount);
    }

    @Override
    @Transactional
    public void modifyName(Long uid, String name) {
        User byName = userDao.getByName(name);
        AssertUtil.isEmpty(byName, "用户名重复,请更换一个名称");
        UserBackpack nameChangeCard = userBackpackDao.getItemById(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(nameChangeCard, "未获取到改名卡");
        userDao.updateById(UserAdapter.buildModifyName(uid, name));
        userBackpackDao.userItemCard(nameChangeCard);
    }

}
