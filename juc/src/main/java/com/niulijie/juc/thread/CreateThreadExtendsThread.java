package com.niulijie.juc.thread;

/**
 * 创建线程方法一：继承Thread类，重写run方法，调用start方法开始线程
 * 注：线程开启并不一定立即执行，由CPU调度执行
 * @author 86176
 * @create 2021/3/28 19:43
 */
public class CreateThreadExtendsThread extends Thread{
    @Override
    public void run() {
        //run方法线程体
        for (int i = 0; i < 20; i++) {
            System.out.println("我在学习多线程==========="+i);
        }
    }

    /**
     * 我在学习多线程===========8
     * 我在学习多线程===========9
     * 我在学习多线程===========10
     * 我在学习执行主线程************6
     * 我在学习多线程===========11
     * 我在学习多线程===========12
     * @param args
     */
    public static void main(String[] args) {
        CreateThreadExtendsThread createThreadExtendsThread = new CreateThreadExtendsThread();
        createThreadExtendsThread.start();
        for (int i = 0; i < 900; i++) {
            System.out.println("我在学习执行主线程************"+i);
        }
    }
}
