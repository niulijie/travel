package com.niulijie.mdm.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * DistributedRedisLock
 */
@Component
public class DistributedRedisLock {

    @Resource
    private RedissonClient redisson;

    private static final String LOCK_TITLE = "lock:";

    /**
     * 加锁
     *
     * @param lockName
     */
    public boolean acquire(String lockName) throws InterruptedException {
        //声明key对象
        String key = LOCK_TITLE + lockName;
        //获取锁对象
        RLock lock = redisson.getLock(key);
        //获取不到锁直接抛异常
        //tryLock(等待时间，锁过期，时间单位)，
        return lock.tryLock(1, 20, TimeUnit.SECONDS);
        //下面是获取不到锁就等待，一直等到锁释放，根据自己情况考虑使用哪种
        //加锁，并且设置锁过期时间，防止死锁的产生
        //mylock.lock(2, TimeUnit.MINUTES);
    }


    /**
     * 锁的释放
     *
     * @param lockName
     */
    public void release(String lockName) {
        //必须是和加锁时的同一个key
        String key = LOCK_TITLE + lockName;
        //获取所对象
        RLock lock = redisson.getLock(key);
        //释放锁（解锁）
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

}
