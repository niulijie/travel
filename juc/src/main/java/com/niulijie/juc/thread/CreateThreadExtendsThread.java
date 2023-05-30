package com.niulijie.juc.thread;

/**
 * 创建线程方法一：继承Thread类，重写run方法，调用start方法开始线程
 * 注：线程开启并不一定立即执行，由CPU调度执行
 *
 * 一.Thread 和 Runnable 的区别
 * Thread 才是 Java 里对线程的唯一抽象，Runnable 只是对任务（业务逻辑）的抽象。Thread 可以接受任意一个 Runnable 的实例并执行。
 * 二.run()和 start()区别
 * 1.Thread类是Java里对线程概念的抽象，
 *      可以这样理解：我们通过new Thread()其实只是 new 出一个 Thread 的实例，还没有操作系统中真正的线程挂起钩来。
 *      只有执行了 start()方法后，才实现了真正意义上的启动线程。
 * 2.从 Thread 的源码可以看到，
 *      Thread 的 start 方法中调用了 start0()方法，而start0()是个 native 方法，这就说明 Thread#start 一定和操作系统是密切相关的。
 *      start()方法让一个线程进入就绪队列等待分配 cpu，分到 cpu 后才调用实现的 run()方法，start()方法不能重复调用，如果重复调用会抛出IllegalThreadStateException异常
 * 3.而 run 方法是业务逻辑实现的地方，本质上和任意一个类的任意一个成员方法并没有任何区别，可以重复执行，也可以被单独调用
 * @author 86176
 * @create 2021/3/28 19:43
 */
public class CreateThreadExtendsThread extends Thread{
    @Override
    public void run() {
        //run方法线程体 -- 要执行的任务
        for (int i = 0; i < 20; i++) {
            System.out.println(Thread.currentThread().getName() + "我在学习多线程==========="+i);
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
        // 创建线程对象
        CreateThreadExtendsThread createThreadExtendsThread = new CreateThreadExtendsThread();
        createThreadExtendsThread.setName("son thread");
        // 启动线程
        createThreadExtendsThread.start();
        //会抛出异常
        //createThreadExtendsThread.start();
        // 主线程调用
        //createThreadExtendsThread.run();
        //createThreadExtendsThread.run();
        for (int i = 0; i < 900; i++) {
            System.out.println("我在学习执行主线程************"+i);
        }
    }
}
