package com.niulijie.juc.thread;

/**
 * 类说明：如何安全中断线程
 * @author niuli
 */
public class EndThread {


    private static class UseThread extends Thread{

        public UseThread(String threadName) {
            super(threadName);
        }

        @Override
        public void run(){
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName+" interrupt flag :"+isInterrupted());
            // while (!Thread.interrupted()){
            while (!isInterrupted()){
            // while (true){
                System.out.println(threadName + " is running");
                System.out.println(threadName + " inner interrupt flag ="+isInterrupted());
            }
            System.out.println(threadName+" interrupt2 flag :"+isInterrupted());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UseThread endThread = new UseThread("endThread");
        endThread.start();
        Thread.sleep(20);
        // 中断线程，其实设置线程的标志位true
        endThread.interrupt();
    }
}
