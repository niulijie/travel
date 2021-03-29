package com.niulijie.juc.thread;

/**
 * 测试stop
 * 1.建议线程正常停止--利用次数，不建议死循环，如果死循环+等待时间
 * 2.建议标志位--设置标志位
 * 3.
 * @author 86176
 * @create 2021/3/29 22:21
 */
public class ThreadStop implements Runnable{

    /**
     * 标志位
     */
    private Boolean flag = true;

    @Override
    public void run() {
        int i = 0;
        while (flag){
            System.out.println("Thread run------>"+i++);
        }
    }

    /**
     * 设置一个公开的方法切换标志位
     */
    public void stop(){
        this.flag = false;
    }

    /**
     * main----->98
     * Thread run------>46
     * main----->99
     * main----->100
     * Thread run------>47
     * 线程该停止了
     * main----->101
     */
    public static void main(String[] args) {
        ThreadStop threadStop = new ThreadStop();
        new Thread(threadStop).start();
        for (int i = 0; i < 200; i++) {
            System.out.println("main----->"+i);
            if(i==100){
                //调用stop方法
                threadStop.stop();
                System.out.println("线程该停止了");
            }
        }
    }
}
