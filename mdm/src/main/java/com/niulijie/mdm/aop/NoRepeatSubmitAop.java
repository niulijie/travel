package com.niulijie.mdm.aop;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.telecomyt.common.model.NoRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;


/**
 * @author www.gaozz.club
 * @功能描述 aop解析注解
 * @date 2018-11-02
 */
@Aspect
@Component
@Slf4j
public class NoRepeatSubmitAop {

    private static final String LOCK_TITLE = "repeat:";

    @Resource
    private ValueOperations<String,Object> stringRedis;

    /**
     * 切点
     *
     * @param noRepeatSubmit 注解
     */
    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointCut(NoRepeatSubmit noRepeatSubmit) {
    }

    /**
     * 利用环绕通知进行处理重复提交问题
     *
     * @param pjp            ProceedingJoinPoint
     * @param noRepeatSubmit 注解
     * @return Object
     */
    @Around(value = "pointCut(noRepeatSubmit)", argNames = "pjp,noRepeatSubmit")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        long lockMillSeconds = noRepeatSubmit.lockTime();
        //获得request对象
        HttpServletRequest request = httpServletRequest();
        Assert.notNull(request, "request can not null");
        Object[] args = pjp.getArgs();
        Object param = args[0];
        JSONObject jsonObject = JSONUtil.parseObj(param, true);
        // 请求参数base64
        String baseParams = DigestUtils.md5Hex(jsonObject.toString() == null ? "" : jsonObject.toString());
        String key = LOCK_TITLE + request.getServletPath() + ":" + baseParams;
        Boolean boolValue = stringRedis.setIfAbsent(key, 1, lockMillSeconds, TimeUnit.SECONDS);
        if (boolValue != null && boolValue) {
            return pjp.proceed();
        }
        log.error("请求地址：{}，请求参数：{}，重复提交",request.getServletPath(), jsonObject);
        return null;
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