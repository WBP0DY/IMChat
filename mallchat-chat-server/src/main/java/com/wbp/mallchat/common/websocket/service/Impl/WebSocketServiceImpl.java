package com.wbp.mallchat.common.websocket.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wbp.mallchat.common.user.dao.UserDao;
import com.wbp.mallchat.common.user.domain.dto.WSChannelExtraDTO;
import com.wbp.mallchat.common.user.domain.entity.User;
import com.wbp.mallchat.common.user.service.LoginService;
import com.wbp.mallchat.common.websocket.enums.WSRespTypeEnum;
import com.wbp.mallchat.common.websocket.service.WebSocketService;
import com.wbp.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import com.wbp.mallchat.common.websocket.vo.req.WSBaseReq;
import com.wbp.mallchat.common.websocket.vo.resp.WSBaseResp;
import com.wbp.mallchat.common.websocket.vo.resp.ws.WSLoginUrl;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jdk.nashorn.internal.parser.Token;
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
    private static final ConcurrentMap<ChannelHandlerContext, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();
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
    public void connect(ChannelHandlerContext ctx) {
        ONLINE_WS_MAP.put(ctx, new WSChannelExtraDTO());
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
        sendMsg(channel, WebSocketAdapter.buildResp(user, token));
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorize());
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
