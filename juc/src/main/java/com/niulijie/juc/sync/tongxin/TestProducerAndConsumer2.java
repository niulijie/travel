package com.niulijie.juc.sync.tongxin;

/**
 * 测试生产者与消费者模型-->信号灯法
 * 角色：生产者，消费者，产品，缓冲区
 * @author 86176
 * @create 2021/3/30 22:50
 */
public class TestProducerAndConsumer2 {
    public static void main(String[] args) {
        TV tv = new TV();
        new Player(tv).start();
        new Watcher(tv).start();
    }
}

/**
 * 生产者-演员
 */
class Player extends Thread{
    TV tv;
    public Player(TV tv){
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            //一半表演一半广告
            if(i%2==0){
                this.tv.play("快乐大本营播放中");
            }else {
                this.tv.play("抖音");
            }
        }
    }
}

/**
 * 消费者-观众
 */
class Watcher extends Thread{
    TV tv;
    public Watcher(TV tv){
        this.tv = tv;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            tv.watch();
        }
    }
}

/**
 * 产品-节目
 */
class TV{
    //演员表演，观众等待 T
    //观众观看，演员等待 F
    /**
     * 表演的节目
     */
    String voice;
    /**
     * 标志位
     */
    boolean flag = true;
    /**
     * 表演
     */
    public synchronized void play(String voice){
        //如果有表演的时候等待
        if(!flag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("演员表演了："+voice);
        //通知观众观看,通知唤醒
        this.notify();
        this.voice = voice;
        this.flag = !this.flag;
    }

    /**
     * 观看
     */
    public synchronized void watch(){
        //如果没有表演的时候等待
        if(flag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("观看了："+voice);
        //通知演员表演
        this.notify();
        this.flag = !this.flag;
    }
}