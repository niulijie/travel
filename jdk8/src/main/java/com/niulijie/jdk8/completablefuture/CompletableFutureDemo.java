package com.niulijie.jdk8.completablefuture;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 内容看test文件下
 * ----------------------------allOf执行结果---------------------------------------------------
 * 1646652178563	|	26	|	pool-1-thread-7	|	task线程：7
 * 1646652178563	|	28	|	pool-1-thread-9	|	task线程：9
 * 1646652178563	|	25	|	pool-1-thread-6	|	task线程：6
 * 1646652178563	|	22	|	pool-1-thread-3	|	task线程：3
 * 1646652178563	|	23	|	pool-1-thread-4	|	task线程：4
 * 1646652178563	|	22	|	pool-1-thread-3	|	任务3完成！result=3异常e=null
 * 1646652178563	|	27	|	pool-1-thread-8	|	task线程：8
 * 1646652178563	|	29	|	pool-1-thread-10	|	task线程：10
 * 1646652178563	|	20	|	pool-1-thread-1	|	task线程：2
 * 1646652178563	|	29	|	pool-1-thread-10	|	任务10完成！result=10异常e=null
 * 1646652178563	|	27	|	pool-1-thread-8	|	任务8完成！result=8异常e=null
 * 1646652178563	|	23	|	pool-1-thread-4	|	任务4完成！result=4异常e=null
 * 1646652178563	|	25	|	pool-1-thread-6	|	任务6完成！result=6异常e=null
 * 1646652178563	|	26	|	pool-1-thread-7	|	任务7完成！result=7异常e=null
 * 1646652178563	|	28	|	pool-1-thread-9	|	任务9完成！result=9异常e=null
 * 1646652178563	|	20	|	pool-1-thread-1	|	任务2完成！result=2异常e=null
 * 1646652180573	|	21	|	pool-1-thread-2	|	task线程：1
 * 1646652180573	|	21	|	pool-1-thread-2	|	任务1完成！result=1异常e=null
 * 1646652182572	|	24	|	pool-1-thread-5	|	task线程：5
 * 1646652182572	|	24	|	pool-1-thread-5	|	任务5完成！result=5异常e=null
 * 所有任务执行完成触发，结果resultList=[3, 10, 8, 4, 6, 7, 9, 2, 1, 5],耗时=5053
 * ----------------------------anyOf执行结果---------------------------------------------------
 * 1646652349502	|	25	|	pool-1-thread-6	|	task线程：6
 * 1646652349502	|	26	|	pool-1-thread-7	|	task线程：7
 * 1646652349502	|	22	|	pool-1-thread-3	|	task线程：3
 * 1646652349502	|	25	|	pool-1-thread-6	|	任务6完成！result=6异常e=null
 * 1646652349502	|	20	|	pool-1-thread-1	|	task线程：2
 * 1646652349502	|	29	|	pool-1-thread-10	|	task线程：10
 * 1646652349502	|	28	|	pool-1-thread-9	|	task线程：9
 * 1646652349502	|	23	|	pool-1-thread-4	|	task线程：4
 * 1646652349502	|	27	|	pool-1-thread-8	|	task线程：8
 * 1646652349502	|	23	|	pool-1-thread-4	|	任务4完成！result=4异常e=null
 * 1646652349502	|	28	|	pool-1-thread-9	|	任务9完成！result=9异常e=null
 * 1646652349502	|	29	|	pool-1-thread-10	|	任务10完成！result=10异常e=null
 * 1646652349502	|	20	|	pool-1-thread-1	|	任务2完成！result=2异常e=null
 * 所有任务中最快完成的一个任务触发，结果resultList=[6],耗时=1045
 * 1646652349502	|	22	|	pool-1-thread-3	|	任务3完成！result=3异常e=null
 * 1646652349502	|	26	|	pool-1-thread-7	|	任务7完成！result=7异常e=null
 * 1646652349502	|	27	|	pool-1-thread-8	|	任务8完成！result=8异常e=null
 * 1646652351514	|	21	|	pool-1-thread-2	|	task线程：1
 * 1646652351514	|	21	|	pool-1-thread-2	|	任务1完成！result=1异常e=null
 * 1646652353500	|	24	|	pool-1-thread-5	|	task线程：5
 * 1646652353500	|	24	|	pool-1-thread-5	|	任务5完成！result=5异常e=null
 * @author niuli
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        //记录开始时间
        long start = System.currentTimeMillis();
        //定长10线程池
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //任务的个数
        final List<Integer> taskNumList =   Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8, 9, 10);
        //生成多个CompletableFuture任务
        List<String> resultList = new ArrayList<String>();
        CompletableFuture[] completableFutures = taskNumList.stream().map(num -> CompletableFuture.supplyAsync(() -> calc(num), executor)
                .thenApply(h -> Integer.toString(h))
                //如需获取任务完成先后顺序，此处代码即可
                .whenComplete((v, e) -> {
                    //System.out.println("任务"+v+"完成！result="+v+"异常e="+e+"执行时间");
                    printTimeAndThread("任务" + v + "完成！result=" + v + "异常e=" + e);
                    resultList.add(v);
                })
        ).toArray(CompletableFuture[]::new);

        //1.allOf:组合多个CompletableFuture为一个CompletableFuture, 所有子任务全部完成，组合后的任务CompletableFuture才会完成
        /*CompletableFuture.allOf(completableFutures).whenComplete((v,e) -> {
            System.out.println("所有任务执行完成触发，结果resultList=" + resultList + ",耗时=" + (System.currentTimeMillis() - start));
        });*/

        //2.anyOf:组合多个CompletableFuture为一个CompletableFuture, 所有子任务中只要存在一个任务优先完成,，组合后的任务CompletableFuture就会完成
        CompletableFuture.anyOf(completableFutures).whenComplete((v, th) -> {
            System.out.println("所有任务中最快完成的一个任务触发，结果resultList=" + resultList + ",耗时=" + (System.currentTimeMillis() - start));
        });
    }

    /**
     * 根据数字判断线程休眠的时间
     * @param i
     * @return
     */
    public static Integer calc(Integer i) {
        try {
            if (i == 1) {
                //任务1耗时3秒
                Thread.sleep(3000);
            } else if (i == 5) {
                //任务5耗时5秒
                Thread.sleep(5000);
            } else {
                //其它任务耗时1秒
                Thread.sleep(1000);
            }
            printTimeAndThread("task线程：" + i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * 打印线程信息
     * @param tag
     */
    public static void printTimeAndThread(String tag){
        String string = new StringJoiner("\t|\t")
                .add(String.valueOf(System.currentTimeMillis()))
                .add(String.valueOf(Thread.currentThread().getId()))
                .add(Thread.currentThread().getName())
                .add(tag)
                .toString();
        System.out.println(string);
    }
}
