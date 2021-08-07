package com.niulijie.jdk8.forkjoin;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * @author niuli
 */
public class TestForkJoin {
    public static void main(String[] args) {

        /**
         * 调用forkJoin框架
         */
        Instant start = Instant.now();

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCalculate(0,10000000000L);
        Long sum = forkJoinPool.invoke(task);
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println("耗费时间为："+ Duration.between(start,end).toMillis());

        /**
         * java8 并发流
         */
        Instant start1 = Instant.now();

        LongStream.rangeClosed(0,10000000000L)
                //并发流
                .parallel()
                //串行流-顺序流
                //.sequential()
                .reduce(0,Long::sum);

        Instant end1 = Instant.now();
        System.out.println("耗费时间为："+ Duration.between(start1,end1).toMillis());
    }
}
