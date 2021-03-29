package com.niulijie.juc.sync;

import java.util.ArrayList;
import java.util.List;

/**
 * 线程不安全的集合
 * @author 86176
 * @create 2021/3/30 0:57
 */
public class UnsafeList {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        //开一万个线程往里面放数据
        for (int i = 0; i < 10000; i++) {
            new Thread(()->{
                synchronized (list){
                    list.add(Thread.currentThread().getName());
                }
            }).start();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
    }

}
