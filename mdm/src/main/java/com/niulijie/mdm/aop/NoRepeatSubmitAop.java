package com.niulijie.mdm.aop;

import com.niulijie.mdm.dto.param.NoRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * @author www.gaozz.club
 * @功能描述 aop解析注解
 * @date 2018-11-02
 */
//@Aspect
//@Component
@Slf4j
public class NoRepeatSubmitAop {

   /* @Resource
    private ValueOperations<String,Object> stringRedis;*/

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
     * @throws Throwable
     */
    @Around(value = "pointCut(noRepeatSubmit)", argNames = "pjp,noRepeatSubmit")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        long lockMillSeconds = noRepeatSubmit.lockTime();
        //获得request对象
        HttpServletRequest request = httpServletRequest();
        Assert.notNull(request, "request can not null");
        // 此处可以用token
        String token = request.getHeader("Authorization");
        String path = request.getServletPath();
        String key = getKey(token, path);
        log.info("key={}", key);
        /*if (stringRedis.get(key) == null)  {
            Object result = pjp.proceed();
            stringRedis.set(key, path, lockMillSeconds, TimeUnit.SECONDS);
            return result;
        } else {
            log.error("{}在{}重复提交",token,path);
            throw new BusinessException(Result.NOT_FOUND.getCode(),"请不要重复提交");
        }*/
        return pjp.proceed();
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

    /**
     * 获得请求key
     *
     * @param token token
     * @param path  路径
     * @return 组合key
     */
    private String getKey(String token, String path) {
        return token + ":" + path;
    }

}