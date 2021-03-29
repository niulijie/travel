package com.niulijie.juc.thread;

/**
 * 测试线程join方法
 * @author 86176
 * @create 2021/3/29 22:56
 */
public class ThreadJoin implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("VIP来了---------->"+i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //启动线程
        ThreadJoin threadJoin = new ThreadJoin();
        Thread thread = new Thread(threadJoin);
        thread.start();

        for (int i = 0; i < 500; i++) {
            System.out.println("main--->"+i);
            if(i==50){
                //线程插队
                thread.join();
            }
        }
    }
}
