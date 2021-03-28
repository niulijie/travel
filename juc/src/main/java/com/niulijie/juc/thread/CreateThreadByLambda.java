package com.niulijie.juc.thread;

/**
 * @author 86176
 * @create 2021/3/28 23:41
 */
public class CreateThreadByLambda{

    public static void main(String[] args) {
        new Thread(()-> System.out.println("lambda表达式创建线程"));
    }

}
