package com.niulijie.juc.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 创建线程方法三：实现callable接口
 * callable的好处：
 * 1.可以定义返回值
 * 2.可以抛出异常
 * @author 86176
 * @create 2021/3/28 21:30
 */
public class CreateThreadByCallable implements Callable<String>{

    private String str;

    public CreateThreadByCallable(String str){
        this.str = str;
    }

    @Override
    public String call() throws Exception {
        return "call" + str;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CreateThreadByCallable t1 = new CreateThreadByCallable("1");
        CreateThreadByCallable t2 = new CreateThreadByCallable("2");
        CreateThreadByCallable t3 = new CreateThreadByCallable("3");
        // 创建执行服务
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        //提交执行
        Future<String> submit1 = executorService.submit(t1);
        Future<String> submit2 = executorService.submit(t2);
        Future<String> submit3 = executorService.submit(t3);
        //获取执行结果
        String s1 = submit1.get();
        String s2 = submit2.get();
        String s3= submit3.get();
        System.out.println("返回结果为------------>s1:"+s1+"--->s2:"+s2+"--->s3:"+s3);
        //关闭服务
        executorService.shutdownNow();
    }
}
