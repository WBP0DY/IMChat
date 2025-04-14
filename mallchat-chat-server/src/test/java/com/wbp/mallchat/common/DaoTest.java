package com.wbp.mallchat.common;

import com.wbp.mallchat.common.user.dao.UserDao;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.service.LoginService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.velocity.runtime.parser.node.PublicFieldExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DaoTest {
    @Autowired
    private UserDao userDao;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolExecutor;

    @Autowired
    private LoginService loginService;

    @Test
    public void getUser () {
        User byId = userDao.getById(1);
        System.out.println(byId);
    }

    @Test
    public void testVoid () throws WxErrorException {
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(1, 1000);
        String url = wxMpQrCodeTicket.getUrl();
        System.out.println(url);
    }

    @Test
    public void redisTest () {
        redisTemplate.opsForValue().set("姓名", "卷心菜");
    }

    @Test
    public void redisTest1 () {
        System.out.println(redisTemplate.opsForValue().get("卷心菜"));
    }

    @Test
    public void test1() throws InterruptedException {
        threadPoolExecutor.execute(() -> {
                    if (1 == 1) {
                        throw new RuntimeException("123");
                    }
                }
        );
    }

    @Test
    public void jwt () {
        System.out.println(loginService.login(3L));
    }
}
