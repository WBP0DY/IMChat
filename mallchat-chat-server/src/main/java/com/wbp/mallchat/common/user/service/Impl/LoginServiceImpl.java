package com.wbp.mallchat.common.user.service.Impl;

import com.wbp.mallchat.common.user.service.LoginService;
import com.wbp.mallchat.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void renewalTokenIfNecessary(String token) {

    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        return 0L;
    }
}
