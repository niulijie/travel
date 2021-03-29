package com.niulijie.juc.thread;

/**
 * 测试守护线程-上帝守护你
 * @author 86176
 * @create 2021/3/29 23:48
 */
public class DaemonThread {
    /**
     * 你一生都开心的活着
     * 你一生都开心的活着
     * 上帝保佑你
     * 上帝保佑你
     * 上帝保佑你
     * googbye world
     * 上帝保佑你
     * 上帝保佑你
     */
    public static void main(String[] args) {
        God god = new God();
        You you = new You();

        Thread thread = new Thread(god);
        //默认是false表示是用户线程，正常的线程都是用户线程
        thread.setDaemon(true);
        //上帝守护线程启动
        thread.start();
        //你--用户线程启动
        new Thread(you).start();
    }
}
/**
 * 上帝
 */
class God implements Runnable{
    @Override
    public void run() {
        while (true){
            System.out.println("上帝保佑你");
        }
    }
}
/**
 * 你
 */
class You implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 365; i++) {
            System.out.println("你一生都开心的活着");
        }
        System.out.println("googbye world");
    }
}