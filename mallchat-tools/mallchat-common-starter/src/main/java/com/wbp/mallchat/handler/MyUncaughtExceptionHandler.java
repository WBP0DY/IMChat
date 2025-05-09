package com.wbp.mallchat.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread" +t.getName(), e);
    }
}
