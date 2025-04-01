package com.wbp.mallchat.common;

import com.wbp.mallchat.common.user.service.LoginService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginServerImplTest {
    @Autowired
    LoginService loginService;

    @Test
    public void test_redis_token() {
        Long uid = 3L;
        String loginToken = loginService.login(uid);
        Long validUid = loginService.getValidUid(loginToken);
        Assert.assertEquals(uid, validUid);
    }
}
