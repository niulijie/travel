import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(classes = Application.class)
public class CompletionServiceTest {

    /**
     * 1646389000981	|	26	|	pool-1-thread-4	|	正在运行,休眠时间=800ms
     * 1646389000981	|	24	|	pool-1-thread-2	|	正在运行,休眠时间=1800ms
     * 1646389000981	|	23	|	pool-1-thread-1	|	正在运行,休眠时间=1200ms
     * 1646389000981	|	25	|	pool-1-thread-3	|	正在运行,休眠时间=200ms
     * 1646389000981	|	28	|	pool-1-thread-6	|	正在运行,休眠时间=1800ms
     * 1646389000981	|	27	|	pool-1-thread-5	|	正在运行,休眠时间=600ms
     * take执行结果 pool-1-thread-3 is Done
     * take执行结果 pool-1-thread-5 is Done
     * take执行结果 pool-1-thread-4 is Done
     * take执行结果 pool-1-thread-1 is Done
     * take执行结果 pool-1-thread-2 is Done
     * take执行结果 pool-1-thread-6 is Done
     *
     */
    @Test
    void testCompletionService(){
        //创建6个任务
        List<Worker> workers = new ArrayList<>();
        AtomicInteger sum = new AtomicInteger();
        for (int i = 1; i <= 6; i++) {
            workers.add(new Worker(new Random().nextInt(10)));
        }

        //创建线程池ExecutorCompletionService,传入一个线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

        //循环提交任务
        workers.forEach(completionService::submit);

        //循环获取每个任务结果
        workers.forEach(worker -> {
            try {
                Future<String> take = completionService.take();
                sum.getAndIncrement();
                String result = take.get();
                System.out.println("take执行结果 " + result);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("获取结果失败");
                e.printStackTrace();
            }
        });

        //执行任务数
        if(completionService.poll() == null && sum.get() == workers.size()){
            System.out.println("任务全部执行完成");
        }
        // 关闭线程池
        executorService.shutdown();
    }
}

class Worker implements Callable<String> {

    private int index;

    public Worker(int index){
        this.index = index;
    }
    @Override
    public String call() throws Exception {
        //休眠毫秒
        int sleepTime = 200 * index;
        printTimeAndThread("正在运行,休眠时间="+sleepTime+"ms");
        sleepMillis(sleepTime);
        //返回结果
        return Thread.currentThread().getName() + " is Done";
    }

    /**
     * 打印线程信息
     * @param tag
     */
    public void printTimeAndThread(String tag){
        String string = new StringJoiner("\t|\t")
                .add(String.valueOf(System.currentTimeMillis()))
                .add(String.valueOf(Thread.currentThread().getId()))
                .add(Thread.currentThread().getName())
                .add(tag)
                .toString();
        System.out.println(string);
    }

    /**
     * 线程睡眠
     * @param millis
     */
    public void sleepMillis(long millis){
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
