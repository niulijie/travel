package com.niulijie.juc.sync;

/**
 * 不安全的买票
 * @author 86176
 * @create 2021/3/30 0:19
 */
public class UnsafeBuyTicket {
    /**
     * 我拿到了第6张票
     * 你拿到了第6张票
     * 你拿到了第5张票
     * 我拿到了第4张票
     * 黄牛党拿到了第4张票
     */
    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();
        new Thread(buyTicket,"你").start();
        new Thread(buyTicket,"我").start();
        new Thread(buyTicket,"黄牛党").start();
    }
}

class BuyTicket implements Runnable{
    /**
     * 票
     */
    private int ticketNum = 10;
    /**
     * 外部停止位
     */
    private Boolean flag = true;

    @Override
    public void run() {
        //买票
        while (flag){
            try {
                buy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * synchronized 同步方法，锁的是this
     * @throws InterruptedException
     */
    private synchronized void buy() throws InterruptedException {
        //判断是否有票
        if(ticketNum <= 0){
            flag = false;
            return;
        }
        //模拟延时
        Thread.sleep(1000);
        //买票
        System.out.println(Thread.currentThread().getName()+"拿到了第"+ticketNum--+"张票");
    }
}