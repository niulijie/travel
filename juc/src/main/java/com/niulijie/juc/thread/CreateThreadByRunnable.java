package com.niulijie.juc.thread;

/**
 * 创建线程方式2：实现runnable接口，重写run方法，执行线程需要丢入runnable接口实现类，交给 Thread 运行,调用.start方法
 * @author 86176
 * @create 2021/3/28 20:21
 */
public class CreateThreadByRunnable implements Runnable{

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("我是小可爱"+i);
        }
    }

    /**
     * 我是小可爱13
     * 我是小可爱14
     * 你是傻子46
     * 我是小可爱15
     * 我是小可爱16
     * @param args
     */
    public static void main(String[] args) {
        //创建runnable接口实现类
        CreateThreadByRunnable createThreadByRunnable = new CreateThreadByRunnable();

        //创建线程对象，通过线程对象来开启我们的线程，代理
//        Thread thread = new Thread(createThreadByRunnable);
//        thread.start();

        new Thread(createThreadByRunnable).start();

        for (int i = 0; i < 1000; i++) {
            System.out.println("你是傻子"+i);
        }

        new Thread(()-> System.out.println("lambda表达式创建线程")).start();
    }
}
