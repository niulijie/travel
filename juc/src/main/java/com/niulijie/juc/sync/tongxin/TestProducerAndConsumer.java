package com.niulijie.juc.sync.tongxin;

/**
 * 测试生产者与消费者模型-->利用缓冲区解决：管程法
 * 角色：生产者，消费者，产品，缓冲区
 * @author 86176
 * @create 2021/3/30 22:50
 */
public class TestProducerAndConsumer {
    public static void main(String[] args) {
        SyncContainer container = new SyncContainer();
        new Producer(container).start();
        new Consumer(container).start();
    }
}

/**
 * 生产者
 */
class Producer extends Thread{
    SyncContainer container;
    public Producer(SyncContainer container){
        this.container = container;
    }

    /**
     * 生产
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            container.push(new Chicken(i));
            System.out.println("生产了"+i+"只鸡");
        }
    }
}

/**
 * 消费者
 */
class Consumer extends Thread{
    SyncContainer container;
    public Consumer(SyncContainer container){
        this.container = container;
    }

    /**
     * 消费
     */
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("消费了----->"+container.pop().id+"只鸡");
        }
    }
}

/**
 * 产品
 */
class Chicken {
    /**
     * 产品编号
     */
    int id;
    public Chicken(int id) {
        this.id = id;
    }
}

/**
 * 缓冲区
 */
class SyncContainer{
    /**
     * 容器大小
     */
    Chicken[] chickens = new Chicken[10];
    /**
     * 容器计数器
     */
    int count = 0;
    /**
     * 生产者放入产品
     */
    public synchronized void push(Chicken chicken){
        //如果容器满了，需要等待消费者消费
        if(count == chickens.length){
            //通知消费者消费,生产者等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            //如果没满，需要丢入产品
            chickens[count] = chicken;
            count++;
            //通知消费者消费
            this.notifyAll();
        }
    }
    /**
     * 消费者消费产品
     */
    public synchronized Chicken pop(){
        //判断是否消费
        if(count==0){
            //等待生产者生产，消费者等待
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //如果可以消费
        count--;
        Chicken chicken = chickens[count];

        //吃完了，通知生产者生产
        this.notifyAll();
        return chicken;
    }
}