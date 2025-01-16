package com.niulijie.juc.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * 如何保证线程 T1、T2、T3 顺序执行”展开，分析了其考察意图，包括多线程基础知识等
 * 介绍了6种保证线程顺序执行的方法：join()、CountDownLatch、Semaphore、单线程池、synchronized、CompletableFuture
 * @author niuli
 */
public class TestThreadSort {

    public static void main(String[] args) {
        new TestThreadJoin().test();
        new TestThreadCountDownLatch().test();
        new TestThreadSemaphore().test();
        new TestThreadVolatile().test();
        new TestThreadCyclicBarrier().test();
    }
}

/**
 * join()方法是Thread类的一部分，可以让一个线程等待另一个线程完成执行。 当你在一个线程T上调用T.join()时，调用线程将进入等待状态，直到线程T完成（即终止）。因此，可以通过在每个线程启动后调用join()来实现顺序执行
 *
 */
class TestThreadJoin {
    public void test(){
        Thread t1 = new Thread(() -> {
            // 线程T1的任务
            System.out.println("t1");
        });
        Thread t2 = new Thread(() -> {
            // 线程T2的任务
            System.out.println("t2");
        });
        Thread t3 = new Thread(() -> {
            // 线程T3的任务
            System.out.println("t3");
        });
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start(); // 等待t2完成
        try {
            t2.join(); // 等待t1完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t3.start();
        try {
            t3.join(); // 等待t3完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

/**
 * CountDownLatch通过一个计数器来实现，初始时，计数器的值由构造函数设置，每次调用countDown()方法，计数器的值减1。当计数器的值变为零时，所有等待在await()方法上的线程都将被唤醒，继续执行。
 * CountDownLatch是Java并发包（java.util.concurrent）中的一个同步辅助类，用于协调多个线程之间的执行顺序。它允许一个或多个线程等待另外一组线程完成操作
 * CountDownLatch关键方法解析：
 *
 * CountDownLatch(int count) : 构造函数，创建一个CountDownLatch实例，计数器的初始值为count。
 * void await() : 使当前线程等待，直到计数器的值变为零。
 * boolean await(long timeout, TimeUnit unit) : 使当前线程等待，直到计数器的值变为零或等待时间超过指定的时间。
 * void countDown() : 递减计数器的值。当计数器的值变为零时，所有等待的线程被唤醒。
 */
class TestThreadCountDownLatch {
    public void test(){
        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            // 线程T1的任务
            System.out.println("t1");
            latch1.countDown(); // 完成后递减latch1
        });

        Thread t2 = new Thread(() -> {
            try {
                latch1.await(); // 等待T1完成
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 线程T2的任务
            System.out.println("t2");
            latch2.countDown(); // 完成后递减latch2
        });

        Thread t3 = new Thread(() -> {
            try {
                latch2.await();// 等待T2完成
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 线程T3的任务
            System.out.println("t3");
        });
        t3.start();
        t2.start();
        t1.start();
    }
}

/**
 * Semaphore通过一个计数器来管理许可，计数器的初始值由构造函数指定，表示可用许可的数量。线程可以通过调用acquire()方法请求许可，
 * 如果许可可用则授予访问权限，否则线程将阻塞。使用完资源后，线程调用release()方法释放许可，从而允许其他阻塞的线程获取许可。
 *
 * Semaphore关键方法分析：
 * Semaphore(int permits) ：构造一个具有给定许可数的Semaphore。
 * Semaphore(int permits, boolean fair) ：构造一个具有给定许可数的Semaphore，并指定是否是公平的。公平性指的是线程获取许可的顺序是否是先到先得。
 * void acquire() ：获取一个许可，如果没有可用许可，则阻塞直到有许可可用。
 * void acquire(int permits) ：获取指定数量的许可。
 * void release() ：释放一个许可。
 * void release(int permits) ：释放指定数量的许可。
 * int availablePermits() ：返回当前可用的许可数量。
 * boolean tryAcquire() ：尝试获取一个许可，立即返回true或false。
 * boolean tryAcquire(long timeout, TimeUnit unit) ：在给定的时间内尝试获取一个许可。
 *
 */
class TestThreadSemaphore {
    public void test(){
        Semaphore semaphore1 = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);

        Thread t1 = new Thread(() -> {
            // 线程T1的任务
            System.out.println("t1");
            semaphore1.release(); // 释放一个许可
        });

        Thread t2 = new Thread(() -> {
            try {
                semaphore1.acquire(); // 获取许可，等待T1完成
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 线程T2的任务
            System.out.println("t2");
            semaphore2.release(); // 释放一个许可
        });

        Thread t3 = new Thread(() -> {
            try {
                semaphore2.acquire(); // 获取许可，等待T2完成
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 线程T3的任务
            System.out.println("t3");
        });
        t3.start();
        t2.start();
        t1.start();
    }
}

class TestThreadCyclicBarrier {
    public void test(){
        // 初始化 CyclicBarrier，设置参与线程的数量为 3
        CyclicBarrier barrier = new CyclicBarrier(3);

        Thread t1 = new Thread(() -> {
            try {
                System.out.println("t1 is running");
                // 等待其他线程到达屏障
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                // 等待t1到达屏障
                barrier.await();
                System.out.println("t2 is running");
                // 等待其他线程到达屏障
                barrier.await();
                System.out.println("t2 continues after barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                // 等待t1、t2到达屏障
                barrier.await();
                System.out.println("t3 is running");
                // 等待其他线程到达屏障
                barrier.await();
                System.out.println("t3 continues after barrier");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }
}

class TestThreadVolatile {

    /**
     * volatile 关键字确保了该变量在多个线程之间的可见性，当一个线程修改了 trigger 的值，其他线程可以立即看到这个修改
     * volatile 关键字仅保证变量的可见性，不保证原子性。如果需要保证多个操作的原子性，应该使用 Atomic 类或其他并发控制机制
     */
    private static volatile boolean trigger = false;

    public void test(){
        Thread t1 = new Thread(() -> {
            // 等待触发器被设置为 true
            while (!trigger) {
                // do nothing 可以添加一些等待策略，避免 CPU 空转
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("t1 is running after trigger is set.");
        });
        Thread t2 = new Thread(() -> {
            // 模拟一些前置操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 设置触发器为 true
            trigger = true;
            System.out.println("t2 sets the trigger.");
        });

        t1.start();
        t2.start();
    }
}

