package com.niulijie.mdm.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.niulijie.mdm.dto.param.DistributedLock;
import com.niulijie.mdm.util.DistributedRedisLock;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 锁aop
 */
@Aspect
@Component
public class DistributedLockAspect {

    @Resource
    private DistributedRedisLock distributedRedisLock;

    /**
     * 切点
     *
     * @param distributedLock 注解
     */
    @Pointcut("@annotation(distributedLock)")
    public void pointCut(DistributedLock distributedLock) {
    }

    /**
     * 方法执行前锁,执行结束后释放锁,如果未获取到锁的key则不加锁
     * annotation内放自定义注解
     */
    @Around(value = "pointCut(distributedLock)", argNames = "joinPoint,distributedLock")
    public Object doAround(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        //获得request对象
        HttpServletRequest request = httpServletRequest();
        Assert.notNull(request, "request can not null");
        Object[] args = joinPoint.getArgs();
        Object param = args[0];
        JSONObject jsonObject = JSONUtil.parseObj(param, true);
        // 请求参数base64
        String baseParams = DigestUtils.md5Hex(jsonObject.toString() == null ? "" : jsonObject.toString());
        String key = request.getServletPath() + ":" + baseParams;
        Object object = null;
        try {
            //加锁
            boolean acquire = distributedRedisLock.acquire(key);
            if (acquire) {
                //执行方法
                object = joinPoint.proceed();
            }
        } finally {
            //循环将所有锁释放
            distributedRedisLock.release(key);
        }
        return object;
    }

    /**
     * 获得request对象
     *
     * @return HttpServletRequest对象
     */
    private HttpServletRequest httpServletRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }
}