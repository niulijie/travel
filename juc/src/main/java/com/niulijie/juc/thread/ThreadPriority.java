package com.niulijie.juc.thread;

/**
 * 测试线程优先级
 * @author 86176
 * @create 2021/3/29 23:27
 */
public class ThreadPriority implements Runnable{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"----->"+Thread.currentThread().getPriority());
    }

    /**
     * main----->5
     * 1----->5
     * 2----->1
     * 4----->10
     * 3----->4
     * Exception in thread "main" Disconnected from the target VM, address: '127.0.0.1:60631', transport: 'socket'
     * java.lang.IllegalArgumentException
     * 	at java.lang.Thread.setPriority(Thread.java:1089)
     * 	at com.niulijie.juc.thread.ThreadPriority.main(ThreadPriority.java:41)
     * @param args
     */
    public static void main(String[] args) {
        //主线程默认优先级
        System.out.println(Thread.currentThread().getName()+"----->"+Thread.currentThread().getPriority());
        ThreadPriority threadPriority = new ThreadPriority();
        //创建线程
        Thread thread1 = new Thread(threadPriority,"1");
        Thread thread2 = new Thread(threadPriority,"2");
        Thread thread3 = new Thread(threadPriority,"3");
        Thread thread4 = new Thread(threadPriority,"4");
        Thread thread5 = new Thread(threadPriority,"5");
        Thread thread6 = new Thread(threadPriority,"6");

        //先设置优先级再启动
        thread1.start();

        //最低为1
        thread2.setPriority(Thread.MIN_PRIORITY);
        thread2.start();

        thread3.setPriority(4);
        thread3.start();

        //最高为10
        thread4.setPriority(Thread.MAX_PRIORITY);
        thread4.start();

        thread5.setPriority(-1);
        thread5.start();

        thread6.setPriority(11);
        thread6.start();
    }
}
