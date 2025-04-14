package com.wbp.mallchat.common.utils;

import com.wbp.mallchat.common.domain.dto.RequestInfo;

public class RequestHolder {
    public static ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();
    public static void set (RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static RequestInfo get () {
        return threadLocal.get();
    }

    public static void remove () {
        threadLocal.remove();
    }
}
