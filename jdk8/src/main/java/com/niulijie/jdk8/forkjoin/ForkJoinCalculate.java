package com.niulijie.jdk8.forkjoin;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * @author niuli
 *
 * ForkJoin框架
 */
public class ForkJoinCalculate extends RecursiveTask<Long> {

    private long start;
    private long end;

    /**
     * 临界值
     */
    private static final long THRESHOLD = 10000;

    public ForkJoinCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * The main computation performed by this task.
     */
    @Override
    protected Long compute() {
        long length = end - start;
        if(length <= THRESHOLD){
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        }else {
            long middle = (start + end) / 2;
            ForkJoinCalculate left = new ForkJoinCalculate(start, middle);
            //拆分子任务，同时压入线程队列
            left.fork();

            ForkJoinCalculate right = new ForkJoinCalculate(middle+1, end);
            //拆分子任务，同时压入线程队列
            right.fork();
            return left.join() + right.join();
        }
    }
}
