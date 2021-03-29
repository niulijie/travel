package com.niulijie.juc.sync;

import lombok.Builder;
import lombok.Data;

/**
 * 模拟不安全取钱
 * @author 86176
 * @create 2021/3/30 0:35
 */
public class UnsafeBank {
    public static void main(String[] args) {
        Account account = new Account(500,"结婚基金");

        Drawing you = new Drawing(account, 200, "你");
        Drawing girlFriend = new Drawing(account, 500, "你媳妇");

        you.start();
        girlFriend.start();
    }
}

/**
 * 账户
 */
@Builder
class Account{
    /**
     * 余额
     */
    int money;
    /**
     * 卡号
     */
    String name;
}

/**
 * 取钱
 */
class Drawing extends Thread{
    /**
     * 账户
     */
    Account account;
    /**
     * 取了多少钱
     */
    int drawingMoney;
    /**
     * 还剩多少钱
     */
    int nowMoney;

    public Drawing(Account account,int drawingMoney,String name){
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }

    /**
     * 取钱
     */
    @Override
    public void run() {
        //这里不能锁this，this是指run方法，必须锁变化的量，即需要增删改的对象
        synchronized (account){
            //判断有没有钱
            if(account.money-drawingMoney<0){
                System.out.println(Thread.currentThread().getName()+"钱不够，取不了");
                return;
            }
            //模拟延时
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //卡内余额 = 余额-取现
            account.money = account.money - drawingMoney;
            //你手里的钱
            nowMoney = nowMoney + drawingMoney;
            System.out.println(account.name+"余额为"+account.money);
            //Thread.currentThread().getName() = this.getName()
            System.out.println(this.getName()+"手里的钱为"+nowMoney);
        }
    }
}