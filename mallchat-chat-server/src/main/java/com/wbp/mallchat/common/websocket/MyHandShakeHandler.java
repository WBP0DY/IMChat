package com.wbp.mallchat.common.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import com.wbp.mallchat.utils.NettyUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Optional;

public class MyHandShakeHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            final HttpObject httpObject = (HttpObject) msg;
            final HttpRequest req = (HttpRequest) httpObject;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(req.getUri());
            Optional<String> token = Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k -> k.get("token"))
                    .map(CharSequence::toString);
            // token存在
            token.ifPresent(s -> NettyUtil.setAttr(ctx.channel(), NettyUtil.TOKEN, s));
            req.setUri(urlBuilder.getPathStr());
        }
        super.channelRead(ctx, msg);
    }
}
