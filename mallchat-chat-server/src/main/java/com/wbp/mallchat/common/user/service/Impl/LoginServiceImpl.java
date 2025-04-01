package com.wbp.mallchat.common.user.service.Impl;

import com.google.common.base.Strings;
import com.wbp.mallchat.common.constant.RedisKey;
import com.wbp.mallchat.common.user.service.LoginService;
import com.wbp.mallchat.common.utils.JwtUtils;
import com.wbp.mallchat.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    public static final int TIME_EXPIRE_DAYS = 3;

    public static final int TOKEN_RESET_DAYS = 1;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String baseKey = getBaseKey(uid);
        Long expire = RedisUtils.getExpire(baseKey, TimeUnit.DAYS);
        // 获取用户的key是否过期
        if (expire == -2) {
            // 不存在
            return;
        }
        if (expire < TOKEN_RESET_DAYS) {
            // 重新续期
            RedisUtils.set(getBaseKey(uid), token, TIME_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getBaseKey(uid), token, TIME_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)) {
            return null;
        }
        String oldToken = RedisUtils.getStr(getBaseKey(uid));
        if (Objects.isNull(oldToken)) {
            return null;
        }
        return Objects.equals(oldToken, token) ? uid : 0L;
    }

    private static String getBaseKey(Long uid) {
        return RedisKey.getBaseKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
