package com.niulijie.juc.thread.pool;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池配置bean
 * @author dufa
 */
@Configuration
public class UnblockThreadPoolExecutor implements ApplicationListener<ContextClosedEvent> {
    /**
     * 实现多线程的方式
     *  1）继承Thread
     *  2）实现Runnable接口
     *  3）实现Callable接口+FutureTask
     *  4）线程池提交任务
     *      实际开发中。应该蒋所有的多线程异步任务都交给线程池处理。资源控制,性能稳定。
     *
     *  1.如何创建线程池[ExecutorService]
     *      1). Executors.newFixedThreadPool()
     *      2). new ThreadPoolExecutor 七大参数
     *          int corePoolSize: 【10】核心线程数。线程池创建好以后就准备就绪的线程数量。等待来接受异步任务去执行。只要线程池不销毁，核心线程数一直存在。
     *          int maximumPoolSize: 【200】 最大线程数量。控制资源。
     *          long keepAliveTime: 存活时间。如果当前的线程数量大于核心线程数量。释放空闲线程。只要线程空闲大于指定的空闲时间keepAliveTime。释放的线程指的是最大的大小减去核心线程数量。
     *          TimeUnit unit: 存活时间的单位。
     *          BlockingQueue<Runnable> workQueue: 阻塞队列。如果任务有很多（超过了最大线程数），就会将目前多的任务放在队列中。只要有空闲的线程，就会去队列中取出新的任务执行。
     *          ThreadFactory threadFactory: 线程的创建工厂。一般都是默认的。具体是创建线程，例如可以自定义线程的名称等。
     *          RejectedExecutionHandler handler: 如果队列workQueue满了，按照指定的拒绝策略，拒绝执行任务。
     *     3).运行流程
     *          1.线程池创建，准备好core数量的核心线程，准备接收任务。
     *          2.core满了，就将再进来的任务放入阻塞队列中。空闲的core就会自己去阻塞队列获取任务执行。
     *          3.阻塞队列也满了，就会直接开新的线程去执行，最大只能开到max指定的数量。
     *          4.max满了后，就用RejectedExecutionHandler拒绝策略
     *          5.max执行完成后，有很多空闲的线程，在指定的时间keepAliveTime以后，还是空闲的话，就释放max减去core这些线程。
     *  2.案例： 一个线程池core=7,max=20,queue=50,100个并发怎么分配
     *         1).7个任务会立刻执行
     *         1).50个任务会放入到队列中。
     *         1).新开13个线程去执行。
     *         1).剩下的30个使用拒绝策略执行，一般都是丢弃。
     */

    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----4 CPU核心数*2（最优）
     * maximumPoolSize 最大线程池大小----200 （根据业务密集度计算）
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.SECONDS
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(10)====10容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     * 							即当提交第41个任务时(前面线程都没有执行完),
     * 						            任务会交给RejectedExecutionHandler来处理
     *
     * 如何来设置
     * 需要根据几个值来决定
     * tasks ：每秒的任务数，假设为500~1000
     * taskcost：每个任务花费时间，假设为0.1s
     * responsetime：系统允许容忍的最大响应时间，假设为1s
     * 做几个计算
     * corePoolSize = 每秒需要多少个线程处理？
     * threadcount = tasks/(1/taskcost) =tasks*taskcout =  (500~1000)*0.1 = 50~100 个线程。corePoolSize设置应该大于50
     * 根据8020原则，如果80%的每秒任务数小于800，那么corePoolSize设置为80即可
     * queueCapacity = (coreSizePool/taskcost)*responsetime
     * 计算可得 queueCapacity = 80/0.1*1 = 80。意思是队列里的线程可以等待1s，超过了的需要新开线程来执行
     * 切记不能设置为Integer.MAX_VALUE，这样队列会很大，线程数只会保持在corePoolSize大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。
     * maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost)
     * 计算可得 maxPoolSize = (1000-80)/10 = 92
     * （最大任务数-队列容量）/每个线程每秒处理能力 = 最大线程数
     * rejectedExecutionHandler：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理
     * keepAliveTime和allowCoreThreadTimeout采用默认通常能满足
     */
    @Bean("poolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                processors, processors * 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100),
                new CustomThreadFactory(), new CustomRejectedExecutionHandler());
    }

    @Bean("executor")
    public ThreadPoolExecutor threadPoolUnboundedExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                processors, processors * 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                new CustomThreadFactory(), new CustomRejectedExecutionHandler());
    }

    /**
     * 自定义拒绝策略
     */
    static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义线程工厂
     */
    static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger count = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            String threadName =  UnblockThreadPoolExecutor.class.getSimpleName() + count.addAndGet(1);
            t.setName(threadName);
            return t;
        }
    }

    /**
     * 所有 Bean 销毁前，执行 ThreadPool 的关闭
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        //ExecutorService的关闭，没有被Spring监听到，所以手动告知Bean销毁前关闭ExecutorService
        this.threadPoolExecutor().shutdown();
        try {
            this.threadPoolExecutor().awaitTermination(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}