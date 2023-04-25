package com.niulijie.juc.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * 创建线程方法三：实现callable接口
 * 一.callable的好处：
 * 1.可以定义返回值
 * 2.可以抛出异常
 * 二.Callable、Future 和 FutureTask
 * 1.Runnable 是一个接口，在它里面只声明了一个 run()方法，由于 run()方法返回值为 void 类型，所以在执行完任务之后无法返回任何结果。
 * 2.Callable 位于 java.util.concurrent包下，它也是一个接口，在它里面也只声明了一个call()方法，这是一个泛型接口，call()函数返回的类型就是传递进来的 V 类型
 * 3.Future 就是对于具体的 Runnable 或者 Callable 任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过 get 方法获取执行结果，该方法会阻塞直到任务返回结果。
 * 4.因为 Future 只是一个接口，所以是无法直接用来创建对象使用的，因此就有了FutureTask
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
        /**
         * 调用方法1
         * Future 就是对于具体的 Runnable 或者 Callable 任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过 get 方法获取执行结果，该方法会阻塞直到任务返回结果。
         */
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

        /**
         * 调用方法2
         * 1.FutureTask 类实现了 RunnableFuture 接口，
         * 2.RunnableFuture 继承了 Runnable接口和 Future 接口，
         * 3.而 FutureTask 实现了 RunnableFuture 接口。
         * 4.所以它既可以作为 Runnable 被线程执行，又可以作为 Future 得到 Callable 的返回值。
         */
        FutureTask<String> futureTask = new FutureTask<String>(new CreateThreadByCallable("4"));
        new Thread(futureTask).start();
        String s4 = futureTask.get();
        System.out.println("返回结果s4："+s4);
    }
}
