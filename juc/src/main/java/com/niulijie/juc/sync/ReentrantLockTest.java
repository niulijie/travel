package com.niulijie.juc.sync;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 86176
 * @create 2021/3/30 22:12
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        BuyTicketTest buyTicketTest = new BuyTicketTest();

        new Thread(buyTicketTest,"你").start();
        new Thread(buyTicketTest,"我").start();
        new Thread(buyTicketTest,"他").start();
    }
}

class BuyTicketTest implements Runnable{
    int ticketNum = 10;

    /**
     * 定义lock锁保证安全
     * private--私有，
     * final--常量
     */
    private final ReentrantLock lock = new ReentrantLock();
    @Override
    public void run() {
        while (true){
            try {
                lock.lock();
                if(ticketNum>0){
                    System.out.println(ticketNum--);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    break;
                }
            }finally {
                //释放锁
                lock.unlock();
            }
        }
    }
}