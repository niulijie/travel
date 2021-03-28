package com.niulijie.juc.thread;

/**
 * @author 86176
 * @create 2021/3/28 21:10
 */
public class Race implements Runnable{

    /**
     * 胜利者
     */
    private String winner;

    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            //模拟兔子睡觉
            /*if(Thread.currentThread().getName().equals("兔子") && i%200 == 0){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
            //判断比赛是否结束,如果结束就停止程序
            if(gameOver(i)){
                break;
            }
            System.out.println(Thread.currentThread().getName()+"跑了"+i+"步");
        }
    }

    /**
     * 是否完成比赛
     */
    private Boolean gameOver(int steps){
        //判断是否有胜利者
        //如果有胜利者，则直接结束比赛
        if(winner != null){
            return true;
        }
        if(steps >= 100){
            winner = Thread.currentThread().getName();
            System.out.println("winner is"+winner);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Race race = new Race();

        new Thread(race,"兔子").start();
        new Thread(race,"乌龟").start();
    }
}
