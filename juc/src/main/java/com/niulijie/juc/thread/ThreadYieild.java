package com.niulijie.juc.thread;

/**
 * 测试线程礼让
 * 礼让不一定成功，看CUP心情
 * @author 86176
 * @create 2021/3/29 22:48
 */
public class ThreadYieild implements Runnable{

    @Override
    public void run() {
        System.out.println("线程"+Thread.currentThread().getName()+"开始执行");
        //线程礼让
        Thread.yield();
        System.out.println("线程"+Thread.currentThread().getName()+"停止执行");
    }

    /**
     * 线程b开始执行
     * 线程a开始执行
     * 线程a停止执行
     * 线程b停止执行
     * ---------------------
     *
     * @param args
     */
    public static void main(String[] args) {
        ThreadYieild threadYieild = new ThreadYieild();
        new Thread(threadYieild,"a").start();
        new Thread(threadYieild,"b").start();
    }
}
