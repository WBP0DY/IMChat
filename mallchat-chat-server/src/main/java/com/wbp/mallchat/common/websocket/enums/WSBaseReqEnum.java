package com.wbp.mallchat.common.websocket.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum WSBaseReqEnum {

    LOGIN(1, "请求登录二维码"),
    HEARTBEAT(2, "心跳包"),
    AUTHORIZE(3, "登录认证"),
    ;

    private final Integer type;
    private final String desc;

    private static Map<Integer, WSBaseReqEnum> cache;

    static {
        cache = Arrays.stream(WSBaseReqEnum.values()).collect(Collectors.toMap(WSBaseReqEnum::getType, Function.identity()));
    }

    public static WSBaseReqEnum of(Integer type) {
        return cache.get(type);
    }

}
