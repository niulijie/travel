import com.sun.glass.ui.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.StringJoiner;
import java.util.concurrent.*;

/**
 * @author niuli
 */
@SpringBootTest(classes = Application.class)
class CompletableFutureTest {

        /**
         * 1646381489642	|	1	|	main	|	main start
         * 1646381489643	|	1	|	main	|	main end
         * 1646381489644	|	23	|	pool-1-thread-1	|	testSubmit start
         * -------------------------添加future.get()等待子任务执行完成-------------------------------------------
         * 1646381672122	|	1	|	main	|	main start
         * 1646381672124	|	23	|	pool-1-thread-1	|	testSubmit start
         * 1646381674124	|	23	|	pool-1-thread-1	|	testSubmit exit
         * testSubmit result testSubmit异步任务执行完成
         * 1646381674124	|	1	|	main	|	main end
         * -------------------------将false改成true 子线程异常会向上抛出,get方法抛出异常导致主线程异常终止--------------------------
         * 1646381746742	|	1	|	main	|	main start
         * 1646381746744	|	23	|	pool-1-thread-1	|	testSubmit start
         *
         * java.util.concurrent.ExecutionException: java.lang.RuntimeException: testSubmit
         */
        @Test
        void testSubmit() throws ExecutionException, InterruptedException {
                //创建异步线程池
                ExecutorService executorService = Executors.newFixedThreadPool(6);
                Future<String> future = executorService.submit(() -> {
                        printTimeAndThread("testSubmit start");
                        sleepMillis(2000);
                        if (false) {
                                throw new RuntimeException("testSubmit");
                        } else {
                                printTimeAndThread("testSubmit exit");
                                return "testSubmit异步任务执行完成";
                        }
                });
                printTimeAndThread("main start");
                //等待子任务执行完成,如果已完成则直接返回结果
                //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
                System.out.println("testSubmit result "+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * 以Runnable类型为参数，所以返回结果为空。用于没有返回值的任务。
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testRunAsync() throws ExecutionException, InterruptedException {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        printTimeAndThread("testRunAsync start");
                        sleepMillis(2000);
                        if (false) {
                                throw new RuntimeException("testRunAsync");
                        } else {
                                printTimeAndThread("testRunAsync exit");
                        }
                });
                printTimeAndThread("main start");
                //等待子任务执行完成,如果已完成则直接返回结果
                //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
                System.out.println("testRunAsync result "+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * 以Supplier<U>函数式接口类型为参数，返回类型为U。用于有返回值的任务
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testSupplyAsync() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("testSupplyAsync start");
                        sleepMillis(2000);
                        if (false) {
                                throw new RuntimeException("testSupplyAsync");
                        } else {
                                printTimeAndThread("testSupplyAsync exit");
                                return "testSupplyAsync异步任务执行完成";
                        }
                });
                printTimeAndThread("main start");
                //等待子任务执行完成,如果已完成则直接返回结果
                //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
                printTimeAndThread("testSupplyAsync result "+ future.get());
                //System.out.println("testSupplyAsync result "+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * thenCompose 可以用于组合多个CompletableFuture，将前一个任务的返回结果作为下一个任务的参数，它们之间存在着先后顺序--任务串行
         * thenCompose方法会在某个任务执行完成后，将该任务的执行结果作为方法入参然后执行指定的方法，该方法会返回一个新的CompletableFuture实例，如果该CompletableFuture实例的result不为null，
         * 则返回一个基于该result的新的CompletableFuture实例；如果该CompletableFuture实例为null，则，然后执行这个新任务
         * 1646463370401	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646463370401	|	1	|	main	|	main start
         * 1646463370606	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646463370606	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * 1646463370716	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646463370716	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛三 start
         * 1646463370716	|	1	|	main	|	thenCompose result1 牛大 + 牛二
         * 1646463371020	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛三 end
         * 1646463371020	|	1	|	main	|	thenCompose result2 牛大 + 牛二 + 牛三
         * 1646463371020	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void thenCompose() throws ExecutionException, InterruptedException {
                CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(200);
                        printTimeAndThread("牛大 end");
                        return "牛大";
                }).thenCompose(str -> CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(100);
                        printTimeAndThread("牛二 end");
                        return str + " + 牛二";
                }));

                CompletableFuture<String> future = stringCompletableFuture.thenCompose((param) -> CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛三 start");
                        sleepMillis(300);
                        printTimeAndThread("牛三 end");
                        return param + " + 牛三";
                }));

                printTimeAndThread("main start");
                //等待子任务执行完成,如果已完成则直接返回结果
                //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
                printTimeAndThread("thenCompose result1 "+ stringCompletableFuture.get());
                printTimeAndThread("thenCompose result2 "+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * thenApply 表示某个任务执行完成后执行的动作，即回调方法，会将该任务的执行结果即方法返回值作为入参传递到回调方法中
         * theApply 强调的是类型转换，而thenCompose强调的是执行顺序，就是前一个任务结果作为下一个任务的参数
         * 当上一个的CompletableFuture执行完后，将结果传递给函数fn，将fn的结果作为新的CompletableFuture任务的参数，进行新一轮的处理。因此它的功能相当于将CompletableFuture<T>转换成 CompletableFuture<U>
         * 1646638777838	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646638777839	|	1	|	main	|	main start
         * 1646638778047	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646638778047	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * 1646638778159	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646638778159	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 start
         * 1646638778462	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 end
         * 1646638778462	|	1	|	main	|	testThenApply result1 牛大 + 牛二
         * 1646638778462	|	1	|	main	|	testThenApply result2 牛大 + 牛二 + 牛三
         * 1646638778462	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testThenApply() throws ExecutionException, InterruptedException {
                CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(200);
                        printTimeAndThread("牛大 end");
                        return "牛大";
                //处理上一次任务结果
                }).thenApply(str -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(100);
                        printTimeAndThread("牛二 end");
                        return str + " + 牛二";
                //处理上一次任务结果
                }).thenApply(String::toLowerCase);

                CompletableFuture<String> future = stringCompletableFuture.thenApply((param) -> {
                        printTimeAndThread("牛三 start");
                        sleepMillis(300);
                        printTimeAndThread("牛三 end");
                        return param + " + 牛三";
                });

                printTimeAndThread("main start");
                //等待子任务执行完成,如果已完成则直接返回结果
                //如果执行任务异常，则get方法会把之前捕获的异常重新抛出
                printTimeAndThread("testThenApply result1 "+ stringCompletableFuture.get());
                printTimeAndThread("testThenApply result2 "+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * thenAccept 同 thenApply 接收上一个任务的返回值作为参数，但是无返回值 -- 消费结果
         * thenRun 方法没有入参，没有返回值
         * 1646640936033	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646640936033	|	1	|	main	|	main start
         * 1646640936336	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646640936336	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * 1646640936541	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646640936541	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 start
         * thenAccept result: 牛大 + 牛二
         * 1646640936952	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 end
         * 1646640936952	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛四 start
         * thenRun do something
         * 1646640937458	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛四 end
         * 1646640937458	|	1	|	main	|	testAcceptAndRun :null
         * 1646640937458	|	1	|	main	|	main end
         */
        @Test
        void testAcceptAndRun() throws ExecutionException, InterruptedException {
                CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(300);
                        printTimeAndThread("牛大 end");
                        return "牛大";
                }).thenApply(s -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(200);
                        printTimeAndThread("牛二 end");
                        return s + " + 牛二";
                }).thenAccept(s -> {
                        printTimeAndThread("牛三 start");
                        sleepMillis(400);
                        System.out.println("thenAccept result: " + s);
                        printTimeAndThread("牛三 end");
                }).thenRun(() -> {
                        printTimeAndThread("牛四 start");
                        sleepMillis(500);
                        System.out.println("thenRun do something");
                        printTimeAndThread("牛四 end");
                });
                printTimeAndThread("main start");
                //等待待最后一个thenRun执行完成
                printTimeAndThread("testAcceptAndRun :"+ voidCompletableFuture.get());
                printTimeAndThread("main end");
        }

        /**
         * exceptionally方法指定某个任务执行异常时执行的回调方法，会将抛出异常作为参数传递到回调方法中，如果该任务正常执行则会exceptionally方法返回的CompletionStage的result就是该任务正常执行的结果
         * ----------------------------------抛出异常后，只有exceptionally执行了，thenAccept没有执行------------------------------------------------------------
         * 1646642173735	|	1	|	main	|	main start
         * 1646642174045	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * error stack trace :java.lang.RuntimeException: error test
         * 1646642176058	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646642176058	|	1	|	main	|	exceptionally result :牛二
         * java.util.concurrent.ExecutionException: java.lang.RuntimeException: error test
         *-----------------------------------if(true) 改成if(false)------------------------------------------------------------
         * 1646642649616	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646642649617	|	1	|	main	|	main start
         * 1646642649917	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646642649917	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 start
         * result :牛大
         * 1646642650922	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 end
         * 1646642650922	|	1	|	main	|	exceptionally result :牛大
         * 1646642650922	|	1	|	main	|	thenAccept result :牛大 + 牛三
         * 1646642650922	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testExceptionally() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(300);
                        if (false) {
                                throw new RuntimeException("error test");
                        } else {
                                printTimeAndThread("牛大 end");
                                return "牛大";
                        }

                });
                //future执行异常时，将抛出的异常作为入参传递给回调方法
                CompletableFuture<String> exceptionally = future.exceptionally(throwable -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(2000);
                        System.out.println("error stack trace :" + throwable.getMessage());
                        printTimeAndThread("牛二 end");
                        return "牛二";
                });

                CompletableFuture<String> stringCompletableFuture = future.thenApply(result -> {
                        printTimeAndThread("牛三 start");
                        sleepMillis(1000);
                        System.out.println("result :" + result);
                        printTimeAndThread("牛三 end");
                        return result + " + 牛三";
                });

                printTimeAndThread("main start");
                //exceptionally.get()没有异常时，依然有返回值，就是future的返回值,即该任务正常执行的结果
                printTimeAndThread("exceptionally result :"+ exceptionally.get());
                printTimeAndThread("thenAccept result :"+ stringCompletableFuture.get());
                printTimeAndThread("main end");
        }

        /**
         * whenComplete是当某个任务执行完成后执行的回调方法，会将执行结果或者执行期间抛出的异常传递给回调方法，如果是正常执行则异常为null，回调方法对应的CompletableFuture的result和该任务一致,如果是执行异常，则get方法抛出异常
         * 1646643903362	|	1	|	main	|	main start
         * 1646643903362	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646643903665	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646643903665	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * run success :牛大
         * 1646643905668	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646643905668	|	1	|	main	|	testWhenComplete result :牛大
         * 1646643905668	|	1	|	main	|	main end
         * ----------------------------------if(false) 改成if(true)--------------------------------------------------------
         * 1646643963889	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646643963889	|	1	|	main	|	main start
         * 1646643964196	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * error stack trace :java.lang.RuntimeException: 牛大 error test
         * 1646643966206	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * java.util.concurrent.ExecutionException: java.lang.RuntimeException: 牛大 error test
         *
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testWhenComplete() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(300);
                        if (true) {
                                throw new RuntimeException("牛大 error test");
                        } else {
                                printTimeAndThread("牛大 end");
                                return "牛大";
                        }
                }).whenComplete((a, b) -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(2000);
                        if (b != null) {
                                System.out.println("error stack trace :" + b.getMessage());
                        } else {
                                System.out.println("run success :" + a);
                        }
                        printTimeAndThread("牛二 end");
                });
                printTimeAndThread("main start");
                //CompletableFuture.supplyAsync执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
                printTimeAndThread("testWhenComplete result :"+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * 跟whenComplete基本一致，区别在于handle的回调方法有返回值，且handle方法返回的CompletableFuture的result是回调方法的执行结果或者回调方法执行期间抛出的异常，与原始CompletableFuture的result无关了
         * 1646644352586	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646644352586	|	1	|	main	|	main start
         * 1646644352898	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * error stack trace :java.lang.RuntimeException: 牛大 error test
         * 1646644354909	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646644354909	|	1	|	main	|	testHandle result :run error
         * 1646644354909	|	1	|	main	|	main end
         * ----------------------------if(true) 改成if(false)----------------------------------------------------
         * 1646644469869	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646644469869	|	1	|	main	|	main start
         * 1646644470177	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646644470177	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 start
         * run success :牛大
         * 1646644472186	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛二 end
         * 1646644472186	|	1	|	main	|	testHandle result :run success
         * 1646644472186	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testHandle() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(300);
                        if (false) {
                                throw new RuntimeException("牛大 error test");
                        } else {
                                printTimeAndThread("牛大 end");
                                return "牛大";
                        }
                }).handle((a, b) -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(2000);
                        if (b != null) {
                                System.out.println("error stack trace :" + b.getMessage());
                        } else {
                                System.out.println("run success :" + a);
                        }
                        printTimeAndThread("牛二 end");
                        if(b!=null){
                                return "run error";
                        }else{
                                return "run success";
                        }
                });
                printTimeAndThread("main start");
                //future.get()是handle的返回值，和原任务没关系
                printTimeAndThread("testHandle result :"+ future.get());
                printTimeAndThread("main end");
        }

        /**
         * thenCombine / thenAcceptBoth / runAfterBoth都是将两个CompletableFuture组合起来，只有这两个都正常执行完了才会执行某个任务; 注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果
         * 区别在于:
         * thenCombine会将两个任务的执行结果作为方法入参传递到指定方法中，且该方法有返回值
         * thenAcceptBoth同样将两个任务的执行结果作为方法入参，但是无返回值
         * runAfterBoth没有入参，也没有返回值
         *
         * 1646646252299	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646646252300	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛二 start
         * 1646646252300	|	1	|	main	|	main start
         * 1646646252407	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛二 end
         * 1646646252613	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646646252613	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛五 start
         * 1646646252613	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛五 do something
         * 1646646253621	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛五 end
         * 1646646253621	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛四 start
         * 1646646253621	|	23	|	ForkJoinPool.commonPool-worker-9	|	param1 :牛大,param2 :牛二
         * 1646646254631	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛四 end
         * 1646646254631	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 start
         * 1646646254631	|	23	|	ForkJoinPool.commonPool-worker-9	|	param1 :牛大,param2 :牛二
         * 1646646255644	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛三 end
         * 1646646255644	|	1	|	main	|	thenCombine result :牛大,牛二,牛三
         * 1646646255644	|	1	|	main	|	thenAcceptBoth result :null
         * 1646646255644	|	1	|	main	|	runAfterBoth result :null
         * 1646646255644	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testThenCombine() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(300);
                        printTimeAndThread("牛大 end");
                        return "牛大";
                });

                CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(100);
                        printTimeAndThread("牛二 end");
                        return "牛二";
                });
                //future和future2的异步任务都执行完成后，会将其执行结果作为方法入参传递给thenCombine,有返回值
                CompletableFuture<String> thenCombine = future.thenCombine(future2, (a, b) -> {
                        printTimeAndThread("牛三 start");
                        printTimeAndThread("param1 :" + a + ",param2 :" + b);
                        sleepMillis(1000);
                        printTimeAndThread("牛三 end");
                        return a + "," + b + ",牛三";
                });

                //future和future2的异步任务都执行完成后，会将其执行结果作为方法入参传递给thenAcceptBoth,无返回值
                CompletableFuture<Void> thenAcceptBoth = future.thenAcceptBoth(future2, (a, b) -> {
                        printTimeAndThread("牛四 start");
                        printTimeAndThread("param1 :" + a + ",param2 :" + b);
                        sleepMillis(1000);
                        printTimeAndThread("牛四 end");
                });

                //future和future2的异步任务都执行完成后，无入参,无返回值
                CompletableFuture<Void> runAfterBoth = future.runAfterBoth(future2, () -> {
                        printTimeAndThread("牛五 start");
                        printTimeAndThread("牛五 do something");
                        sleepMillis(1000);
                        printTimeAndThread("牛五 end");
                });

                printTimeAndThread("main start");
                //printTimeAndThread("future result :"+ future.get());
                //printTimeAndThread("future2 result :"+ future2.get());
                printTimeAndThread("thenCombine result :"+ thenCombine.get());
                printTimeAndThread("thenAcceptBoth result :"+ thenAcceptBoth.get());
                printTimeAndThread("runAfterBoth result :"+ runAfterBoth.get());
                printTimeAndThread("main end");
        }

        /**
         * applyToEither / acceptEither / runAfterEither都是将两个CompletableFuture组合起来，只要其中一个执行完了就会执行某个任务;注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果
         * 区别在于:
         * applyToEither会将已经执行完成的任务的执行结果作为方法入参，并有返回值
         * acceptEither同样将已经执行完成的任务的执行结果作为方法入参，但是没有返回值
         * runAfterEither没有方法入参，也没有返回值
         * 1646648005245	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646648005245	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛二 start
         * 1646648005246	|	1	|	main	|	main start
         * 1646648005352	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛二 end
         * 1646648005352	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛五 start
         * 1646648005352	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛五 do something
         * 1646648006364	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛五 end
         * 1646648006364	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛四 start
         * 1646648006364	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛四 param :牛二
         * 1646648007373	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛四 end
         * 1646648007373	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛三 start
         * 1646648007373	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛三 param :牛二
         * 1646648008248	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646648008373	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛三 end
         * 1646648008373	|	1	|	main	|	thenCombine result :牛二,牛三
         * 1646648008373	|	1	|	main	|	thenAcceptBoth result :null
         * 1646648008373	|	1	|	main	|	runAfterBoth result :null
         * 1646648008373	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testApplyToEither() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(3000);
                        printTimeAndThread("牛大 end");
                        return "牛大";
                });

                CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(100);
                        printTimeAndThread("牛二 end");
                        return "牛二";
                });
                //future和future2的异步任务都执行完成后，会将其执行结果作为方法入参传递给thenCombine,有返回值
                CompletableFuture<String> applyToEither = future.applyToEither(future2, (result) -> {
                        printTimeAndThread("牛三 start");
                        printTimeAndThread("牛三 param :" + result);
                        sleepMillis(1000);
                        printTimeAndThread("牛三 end");
                        return result + ",牛三";
                });

                //future和future2的异步任务都执行完成后，会将其执行结果作为方法入参传递给thenAcceptBoth,无返回值
                CompletableFuture<Void> acceptEither = future.acceptEither(future2, (result) -> {
                        printTimeAndThread("牛四 start");
                        printTimeAndThread("牛四 param :" + result);
                        sleepMillis(1000);
                        printTimeAndThread("牛四 end");
                });

                //future和future2的异步任务都执行完成后，无入参,无返回值
                CompletableFuture<Void> runAfterEither = future.runAfterEither(future2, () -> {
                        printTimeAndThread("牛五 start");
                        printTimeAndThread("牛五 do something");
                        sleepMillis(1000);
                        printTimeAndThread("牛五 end");
                });

                printTimeAndThread("main start");
                //printTimeAndThread("future result :"+ future.get());
                //printTimeAndThread("future2 result :"+ future2.get());
                printTimeAndThread("thenCombine result :"+ applyToEither.get());
                printTimeAndThread("thenAcceptBoth result :"+ acceptEither.get());
                printTimeAndThread("runAfterBoth result :"+ runAfterEither.get());
                printTimeAndThread("main end");
        }

        /**
         * allOf() 是多个任务都执行完成后才会执行，只要有一个任务执行异常，返回的CompletableFuture执行get()方法时会抛出异常，如果都是正常执行，则get返回null
         * anyOf() 是多个任务，只要只有一个任务执行完成，无论是正常执行或者执行异常，都会执行回调，返回的CompletableFuture执行get()方法返回的就是已执行完成的任务的执行结果
         * 1646649105251	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 start
         * 1646649105251	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛二 start
         * 1646649105252	|	25	|	ForkJoinPool.commonPool-worker-11	|	牛三 start
         * 1646649105252	|	1	|	main	|	main start
         * 1646649105363	|	24	|	ForkJoinPool.commonPool-worker-2	|	牛二 end
         * run success :牛二
         * 1646649105552	|	23	|	ForkJoinPool.commonPool-worker-9	|	牛大 end
         * 1646649106256	|	25	|	ForkJoinPool.commonPool-worker-11	|	牛三 end
         * run success :null
         * 1646649106256	|	1	|	main	|	testAllOf result :null
         * 1646649106256	|	1	|	main	|	testAnyOf result :牛二
         * 1646649106256	|	1	|	main	|	main end
         * @throws ExecutionException
         * @throws InterruptedException
         */
        @Test
        void testAllOfAndAnyOf() throws ExecutionException, InterruptedException {
                CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛大 start");
                        sleepMillis(300);
                        printTimeAndThread("牛大 end");
                        return "牛大";
                });

                CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛二 start");
                        sleepMillis(100);
                        printTimeAndThread("牛二 end");
                        return "牛二";
                });

                CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
                        printTimeAndThread("牛三 start");
                        sleepMillis(1000);
                        printTimeAndThread("牛三 end");
                        return "牛三";
                });

                CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(future, future2, future3).whenComplete((a, b) -> {
                        if (b != null) {
                                System.out.println("error stack trace :" + b.getMessage());
                        } else {
                                System.out.println("run success :" + a);
                        }
                });

                CompletableFuture<Object> complete = CompletableFuture.anyOf(future, future2, future3).whenComplete((a, b) -> {
                        if (b != null) {
                                System.out.println("error stack trace :" + b.getMessage());
                        } else {
                                System.out.println("run success :" + a);
                        }
                });

                printTimeAndThread("main start");
                printTimeAndThread("testAllOf result :"+ voidCompletableFuture.get());
                printTimeAndThread("testAnyOf result :"+ complete.get());
                printTimeAndThread("main end");
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
