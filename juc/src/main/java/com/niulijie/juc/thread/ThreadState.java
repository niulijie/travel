package com.niulijie.juc.thread;

/**
 * 观察测试线程状态
 * @author 86176
 * @create 2021/3/29 23:04
 */
public class ThreadState {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("**********************");
        });

        //线程状态--NEW
        Thread.State state = thread.getState();
        System.out.println("状态1刚新建时："+state);

        //启动线程-RUNNABLE
        thread.start();
        state = thread.getState();
        System.out.println("状态2调用start方法后："+state);

        //线程没有终止，就一直输入状态
        while (state != Thread.State.TERMINATED){
            Thread.sleep(100);
            //更新线程状态--TIMED_WAITING
            state = thread.getState();
            System.out.println("状态3："+state);
        }
    }
}
