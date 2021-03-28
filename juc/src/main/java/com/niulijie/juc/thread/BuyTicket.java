package com.niulijie.juc.thread;

/**
 * 发现问题：多个线程操作同一个资源的情况下，线程不安全，数据紊乱
 * @author 86176
 * @create 2021/3/28 20:50
 */
public class BuyTicket implements Runnable{

    //票数
    private int ticketNum = 10;


    /**
     * continue 是跳出本次迭代，会继续执行下一个迭代。
     * break是退出本次循环。
     * return是返回函数的结果值，并终止当前函数。
     * ========================================
     * 老师---》拿到了第9张票
     * 小明---》拿到了第7张票
     * 黄牛党---》拿到了第6张票
     * 老师---》拿到了第7张票
     * 黄牛党---》拿到了第5张票
     * 老师---》拿到了第5张票
     */
    @Override
    public void run() {
        while (true){
            if(ticketNum <= 0){
                break;
            }
            try {
                //模拟延时
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"---》拿到了第"+ticketNum--+"张票");
        }
    }

    public static void main(String[] args) {
        BuyTicket buyTicket = new BuyTicket();

        new Thread(buyTicket,"小明").start();
        new Thread(buyTicket,"老师").start();
        new Thread(buyTicket,"黄牛党").start();
    }
}
