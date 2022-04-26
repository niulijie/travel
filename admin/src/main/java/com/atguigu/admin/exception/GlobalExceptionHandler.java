package com.atguigu.admin.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 处理整个web controller异常
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //处理异常
    @ExceptionHandler({ArithmeticException.class, NullPointerException.class})
    public String handlerArithException(Exception e){
        log.info("异常是：{}", e);
        //视图地址
        return "login";
    }
}
