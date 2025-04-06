package com.wbp.mallchat.common.websocket.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wbp.mallchat.common.user.dao.UserDao;
import com.wbp.mallchat.common.user.domain.dto.WSChannelExtraDTO;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.service.LoginService;
import com.wbp.mallchat.common.websocket.service.WebSocketService;
import com.wbp.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import com.wbp.mallchat.common.websocket.vo.resp.WSBaseResp;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    @Lazy
    WxMpService wxMpService;

    @Autowired
    LoginService loginService;

    /**
     * 管理所有用户的连接（登陆态/游客）
     */
    private static final ConcurrentMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
    public static final int MAXIMUM_SIZE = 10000;
    public static final Duration DURATION = Duration.ofHours(1);
    /**
     * 临时保存登录code和channel的映射关系
     */
    public static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();
    @Autowired
    private UserDao userDao;

    @Override
    public void offLine(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        // todo 用户下线
    }

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }
    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        // 生成随机码
        Integer code = getLoginCode(channel);
        WAIT_LOGIN_MAP.put(code, channel);
        // 找微信申请带参二位码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        // 把码推送给前端
        sendMsg(channel, WebSocketAdapter.nulidResp(wxMpQrCodeTicket));
    }

    @Override
    public void scanLoginSuccess(Integer remove, String openid) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(remove);
        if (Objects.isNull(channel)) {
            return;
        }
        User user = userDao.getByOpenID(openid);
        // 移除code
        WAIT_LOGIN_MAP.invalidate(remove);
        // 获取token
        String token = loginService.login(user.getId());
        loginSuccess(channel, token, user);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorize());
    }

    /**
     * 用户进行登录认证
     *
     * @param channel 连接
     * @param token token值
     */
    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            // token有效，返回登录成功
            User userid = userDao.getById(validUid);
            loginSuccess(channel, token, userid);
        } else {
            // token无效，返回用户重新登录
            sendMsg(channel, WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    private void loginSuccess(Channel channel, String token, User userid) {
        // 保存channel对应的uid
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(userid.getId());
        // todo 用户上线成功的事件
        // 推送成功消息
        sendMsg(channel, WebSocketAdapter.buildResp(userid, token));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> wsBaseResp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(wsBaseResp)));
    }

    private Integer getLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }
}
