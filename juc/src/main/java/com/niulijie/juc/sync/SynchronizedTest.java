package com.niulijie.juc.sync;

import org.openjdk.jol.info.ClassLayout;

/**
 * 1.锁消除：在synchronized修饰的代码中，如果不存在操作临界资源的情况，会触发锁消除，你即便写了synchronized，他也不会触发。
 * 2.锁膨胀：如果在一个循环中，频繁的获取和释放锁资源，这样带来的消耗很大，锁膨胀就是将锁的范围扩大，避免频繁的竞争和获取锁资源带来不必要的消耗。
 * 3.锁升级：ReentrantLock的实现，是先基于乐观锁的CAS尝试获取锁资源，如果拿不到锁资源，才会挂起线程。synchronized在JDK1.6之前，完全就是获取不到锁，立即挂起当前线程，所以synchronized性能比较差。
 *      synchronized就在JDK1.6做了锁升级的优化
 *      3.1 无锁、匿名偏向：当前对象没有作为锁存在。
 *      3.2 偏向锁：如果当前锁资源，只有一个线程在频繁的获取和释放，那么这个线程过来，只需要判断，当前指向的线程是否是当前线程 。
 *          如果是，直接拿着锁资源走。
 *          如果当前线程不是我，基于CAS的方式，尝试将偏向锁指向当前线程。如果获取不到，触发锁升级，升级为轻量级锁。（偏向锁状态出现了锁竞争的情况）
 *      3.3 轻量级锁：会采用自旋锁的方式去频繁的以CAS的形式获取锁资源（采用的是自适应自旋锁）
 *          如果成功获取到，拿着锁资源走
 *          如果自旋了一定次数，没拿到锁资源，锁升级。
 *      3.4 重量级锁：就是最传统的synchronized方式，拿不到锁资源，就挂起当前线程。（用户态&内核态）
 */
public class SynchronizedTest {

    /*public static void main(String[] args) {
        // 锁的是当前类Test.class
        Test.a();

        // 锁的是new出来的test对象
        Test test = new Test();
        test.b();
    }*/

    /**
     * 锁默认情况下，开启了偏向锁延迟。
     * 	偏向锁在升级为轻量级锁时，会涉及到偏向锁撤销，需要等到一个安全点（STW），才可以做偏向锁撤销，在明知道有并发情况，就可以选择不开启偏向锁，或者是设置偏向锁延迟开启
     * 	因为JVM在启动时，需要加载大量的.class文件到内存中，这个操作会涉及到synchronized的使用，为了避免出现偏向锁撤销操作，JVM启动初期，有一个延迟4s开启偏向锁的操作
     * 	如果正常开启偏向锁了，那么不会出现无锁状态，对象会直接变为匿名偏向
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        // 为了展示JVM的开启偏向锁延迟操作， -- 匿名偏向00000101 00000000 00000000 00000000
        Thread.sleep(5000);

        // 无锁状态 00000001 00000000 00000000 00000000
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // 模拟锁竞争: 出现重量级锁11101010 10010011 00110101 10101100；
        new Thread(() -> {
            synchronized (o){
                // t1 - 偏向锁
                System.out.println("t1:" + ClassLayout.parseInstance(o).toPrintable());
            }
        }).start();

        // 为了模拟main出现轻量级锁
        Thread.sleep(5000);
        // 轻量级锁 10011000 11110100 01111111 00001100；有40行sleep之后会变成偏向锁00000101 11111000 11101010 00010001；可能也会出现轻量级锁10101000 11110101 11101111 10000001(为了展示加5540行sleep之后main有)
        synchronized (o){
            // 出现锁竞争后 main - 偏向锁（101） - 轻量级锁CAS（00） - 重量级锁（10）
            System.out.println("main:" + ClassLayout.parseInstance(o).toPrintable());
        }
    }
}

class Test{
    /**
     * static方法锁的是当前类Test
     */
    public static synchronized void a(){
        System.out.println("111111");
    }

    /**
     * 非static方法锁的是当前对象this
     */
    public synchronized void b(){
        System.out.println("222222");
    }
}
