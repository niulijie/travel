package com.niulijie.juc.thread.pool.testpool;


import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author niuli
 */
public class TestThreadPoolExecutor {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    public void test(){
        AtomicInteger i = new AtomicInteger();
        threadPoolExecutor.execute(() -> System.out.println("我是:"+ i.getAndIncrement()));
    }
}
