package com.wbp.mallchat.common.websocket;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.wbp.mallchat.utils.NettyUtil;
import com.wbp.mallchat.common.websocket.enums.WSBaseReqEnum;
import com.wbp.mallchat.common.websocket.service.WebSocketService;
import com.wbp.mallchat.common.websocket.vo.req.WSBaseReq;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    WebSocketService webSocketService;
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("握手成功");
            String token = NettyUtil.getAttr(ctx.channel(), NettyUtil.TOKEN);
            if (Objects.nonNull(token)) {
                webSocketService.authorize(ctx.channel(), token);
            }
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
                break;
            case AUTHORIZE:
                webSocketService.authorize(channelHandlerContext.channel(), bean.getData());
                break;
            case HEARTBEAT:
                break;
            default:
                log.info("未知类型");
        }
    }
}
