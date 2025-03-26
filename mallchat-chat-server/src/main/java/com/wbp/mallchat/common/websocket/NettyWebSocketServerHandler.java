package com.wbp.mallchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.wbp.mallchat.common.websocket.enums.WSBaseReqEnum;
import com.wbp.mallchat.common.websocket.service.WebSocketService;
import com.wbp.mallchat.common.websocket.vo.req.WSBaseReq;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    WebSocketService webSocketService;
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            System.out.println("握手完成");
        }else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲");
                // todo 用户下线
                userOffline(ctx.channel());
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
         userOffline(ctx.channel());
    }

    /**
     * 用户下线
     */
    private void userOffline (Channel channel) {
        webSocketService.offLine(channel);
        channel.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        String text = msg.text();
        WSBaseReq bean = JSONUtil.toBean(text, WSBaseReq.class);
        switch (WSBaseReqEnum.of(bean.getType())) {
            case LOGIN:
                webSocketService.handleLoginReq(channelHandlerContext.channel());
            case AUTHORIZE:
                break;
            case HEARTBEAT:
                break;
        }
    }
}
