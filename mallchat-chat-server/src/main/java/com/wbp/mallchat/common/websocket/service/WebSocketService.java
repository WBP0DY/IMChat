package com.wbp.mallchat.common.websocket.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WebSocketService {
    void offLine(Channel channel);

    void connect(ChannelHandlerContext ctx);

    void handleLoginReq(Channel channel) throws WxErrorException;

    void scanLoginSuccess(Integer remove, String openid);

    void waitAuthorize(Integer code);
}
