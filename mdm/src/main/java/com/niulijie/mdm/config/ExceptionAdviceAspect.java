package com.niulijie.mdm.config;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.niulijie.mdm.dto.param.Dept;
import com.niulijie.mdm.dto.param.ExceptionInfo;
import com.niulijie.mdm.dto.param.User;
import com.niulijie.mdm.entity.ServiceErrorLog;
import com.niulijie.mdm.entity.ThirdErrorLog;
import com.niulijie.mdm.mapper.ServiceErrorLogMapper;
import com.niulijie.mdm.mapper.ThirdErrorLogMapper;
import com.niulijie.mdm.util.BeanCopierUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Aspect
@Component
public class ExceptionAdviceAspect {

    @Autowired
    private HttpServletRequest request;

    @Resource
    private ServiceErrorLogMapper serviceErrorLogMapper;

    @Resource
    private ThirdErrorLogMapper thirdErrorLogMapper;

    @AfterThrowing(pointcut = "execution( * com.niulijie.mdm.service..*..*(..))", throwing = "ex")
    public void doRecoveryActions(JoinPoint joinPoint, Throwable ex) {
        try {
            String method = request.getMethod();
            ExceptionInfo exceptionInfo = getExceptionInfo(ex);
            addServiceErrorLog(joinPoint, exceptionInfo, method);
        } catch (Exception e) {
            log.error("非web请求,service报错---无需处理");
        }
    }

    private static ExceptionInfo getExceptionInfo(Throwable ex) {
        ExceptionInfo exceptionInfo = new ExceptionInfo();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        if (ArrayUtils.isNotEmpty(stackTrace)) {
            List<StackTraceElement> stackTraceElements = Arrays.asList(stackTrace);
            StackTraceElement stackTraceElement = stackTraceElements.stream()
                    .filter(e -> e.getClassName().contains("com.niulijie.")).limit(1)
                    .toArray(StackTraceElement[]::new)[0];
            exceptionInfo = BeanCopierUtil.copyBean(stackTraceElement, ExceptionInfo.class);
            exceptionInfo.setErrorInfo(ex.toString());
        }
        return exceptionInfo;
    }

    @Async
    public void addServiceErrorLog(JoinPoint proceedingJoinPoint, ExceptionInfo exceptionInfo, String method) {
        if (method.equals("post") || method.equals("POST")) {
            // 接口路径
            String uri = request.getRequestURL().toString();
            // 入参
            Object[] paramValues = proceedingJoinPoint.getArgs();
            Object paramValue = paramValues[0];
            String errorResult = JSONUtil.toJsonPrettyStr(paramValue);
            serviceErrorLogMapper.insert(ServiceErrorLog.builder()
                    .createTime(LocalDateTime.now())
                    .errorResult(errorResult)
                    .errorUrl(uri)
                    .errorInfo(JSONUtil.toJsonPrettyStr(exceptionInfo))
                    .build());
        }
    }


    @AfterThrowing(pointcut = "execution( * com.niulijie.mdm.listener.MqMessageListener.*(..))", throwing = "ex")
    public void third(JoinPoint joinPoint, Throwable ex) {
        ExceptionInfo exceptionInfo = getExceptionInfo(ex);
        addThirdErrorLog(joinPoint, exceptionInfo);
    }

    @Async
    public void addThirdErrorLog(JoinPoint proceedingJoinPoint, ExceptionInfo exceptionInfo) {
        // 入参
        Object[] paramValues = proceedingJoinPoint.getArgs();
        Object data ;
        Message messageVale = (Message) paramValues[0];
        String consumerQueue = messageVale.getMessageProperties().getConsumerQueue();
        if (consumerQueue.contains("user")){
            data = JSONUtil.toBean(new String(messageVale.getBody()), User.class);
        }else {
            data = JSONUtil.toBean(new String(messageVale.getBody()), Dept.class);
        }
        thirdErrorLogMapper.insert(ThirdErrorLog.builder()
                .createTime(LocalDateTime.now())
                .errorResult(JSONUtil.toJsonStr(data))
                .thirdType(1)
                .errorInfo(JSONUtil.toJsonPrettyStr(exceptionInfo))
                .build());
    }

    @Async
    public void addThirdErrorLogs(JoinPoint proceedingJoinPoint, ExceptionInfo exceptionInfo) throws NoSuchMethodException {
        // 入参
        //1.获取用户行为日志(ip,username,operation,method,params,time,createdTime)
        //获取类的字节码对象，通过字节码对象获取方法信息
        Class<?> targetCls = proceedingJoinPoint.getTarget().getClass();
        //获取方法签名(通过此签名获取目标方法信息)
        MethodSignature ms = (MethodSignature)proceedingJoinPoint.getSignature();
        //获取目标方法上的注解指定的操作名称
        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        thirdErrorLogMapper.insert(ThirdErrorLog.builder()
                .createTime(LocalDateTime.now())
                .errorResult(targetMethod.getName())
                .thirdType(2)
                .errorInfo(JSONUtil.toJsonPrettyStr(exceptionInfo))
                .build());
    }

    @Pointcut("execution(public * com.niulijie.*..*.controller..*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ObjectUtils.isEmpty(ra)) {
            return pjp.proceed();
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        /*
         * 接收到请求==>进入时间: 2022-07-21 18:15:24:042	请求相关: POST	172.16.20.189	http://control.telecomyt.com.cn:8080/mdm/alarmInfo/alarm/list
         * 路径参数: com.telecomyt.mdm.alarm.controller.AlarmInfoController.alarmList[{"adminCancel":0,"size":100,"current":1}]
         * 返回结果: {"code":200,"message":"success","data":{"records":[],"total":0,"size":100,"current":1,"orders":[],"optimizeCountSql":true,"isSearchCount":true},"currentTime":1658398524049}	出去时间: 2022-07-21 18:15:24:049
         */
        // 记录下请求内容
        StringBuilder logBuilder = new StringBuilder("接收到请求==>");
        logBuilder.append("进入时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()))
                .append("\t请求相关: " + request.getMethod() + "\t" + request.getRemoteAddr() + "\t" + request.getRequestURL().toString());
        Object[] args = pjp.getArgs();
        List<Object> argList = new ArrayList<Object>();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                //ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                //ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                continue;
            }
            argList.add(args[i]);
        }
        String paramter = JSONUtil.toJsonStr(argList);
        // 完整文件路径.方法名称 参数
        logBuilder.append("\t路径参数: "+ pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName() + paramter);
        // 处理完请求，返回内容
        Object result = pjp.proceed();
        String logRes = JSONUtil.toJsonStr(result);
        logBuilder.append("\t返回结果: " + logRes).append("\t出去时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date()));
        log.info(logBuilder.toString());
        return result;
    }
}
