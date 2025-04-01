package com.wbp.mallchat.factory;

import com.wbp.mallchat.handler.MyUncaughtExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * 采用装饰器模式，加载线程池调用时抛出异常
 */
@AllArgsConstructor
@NoArgsConstructor
public class MyThreadFactory implements ThreadFactory {
    private ThreadFactory threadFactory;
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = threadFactory.newThread(r);
        thread.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());//异常捕获
        return thread;
    }
}
