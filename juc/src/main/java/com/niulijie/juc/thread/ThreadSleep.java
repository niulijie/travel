package com.niulijie.juc.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 测试线程休眠
 * 1.模拟倒计时
 * @author 86176
 * @create 2021/3/29 22:36
 */
public class ThreadSleep {
    public static void tenDown() throws InterruptedException {
        int num = 10;
        while (true){
            Thread.sleep(1000);
            System.out.println(num--);
            if(num<=0){
                break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            tenDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /**
         * 2.打印系统当前时间
         */
        //获取系统当前时间
        Date startDate = new Date(System.currentTimeMillis());

        while (true){
            try {
                Thread.sleep(1000);
                System.out.println(new SimpleDateFormat("HH:ss:mm").format(startDate));
                //更新当前时间
                startDate = new Date(System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
