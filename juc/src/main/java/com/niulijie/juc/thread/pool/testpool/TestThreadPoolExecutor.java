package com.niulijie.juc.thread.pool.testpool;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author niuli
 */
@RestController
@RequestMapping("/sys/config")
public class TestThreadPoolExecutor {

//    @Qualifier(value = "poolExecutor")
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /*public void test(){
        AtomicInteger i = new AtomicInteger();
        threadPoolExecutor.execute(() -> System.out.println("我是:"+ i.getAndIncrement()));
    }*/

    @PostMapping("/info")
    public List<String> testCountDownLatch() throws InterruptedException {
        List<String> list = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        threadPoolExecutor.execute(() -> {
            for (int j = 0; j < 6; j++) {
                System.out.println(j);
                list.add("一"+ j );
            }
            countDownLatch.countDown();
        });
        threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int j = 6; j < 12; j++) {
                System.out.println(j);
                list.add("二"+ j );
            }
            countDownLatch.countDown();
        });
        threadPoolExecutor.execute(() -> {
            for (int j = 12; j < 18; j++) {
                System.out.println(j);
                list.add("三"+ j );
            }
            countDownLatch.countDown();
        });
        // 保证所有线程执行完成，返回合并数据
        countDownLatch.await();
        return list;
    }
}
