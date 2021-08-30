package com.niulijie.common.handler;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.niulijie.common.dto.BaseResp;
import com.niulijie.common.dto.ResultStatus;
import com.niulijie.common.dto.ResultUtil;
import com.niulijie.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.handler
 * @email zhoupengbing@telecomyt.com.cn
 * @description 全局异常处理类
 * @createTime 2019年12月09日 11:02:00 @Version v1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统异常处理，比如：404,500
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResp<?> defaultErrorHandler(Exception e) {
        BaseResp baseResp = new BaseResp();
        if (e instanceof HttpMessageNotReadableException) {
            baseResp.setCode(ResultStatus.http_status_method_param_error.getErrorCode());
            baseResp.setMessage(ResultStatus.http_status_method_param_error.getErrorMsg());
        } else if (e instanceof ExcelAnalysisException) {
            baseResp.setMessage(e.getMessage());
            baseResp.setCode(ResultStatus.http_status_bad_request.getErrorCode());
        } else {
            baseResp.setMessage(ResultStatus.http_status_internal_server_error.getErrorMsg());
            baseResp.setCode(ResultStatus.http_status_internal_server_error.getErrorCode());
        }
        e.printStackTrace();
        log.error("系统出现未知错误，错误信息为：" + e.toString());
        baseResp.setData("");
        baseResp.setCurrentTime(System.currentTimeMillis());
        return baseResp;
    }

    /**
     * 这里处理Get请求多参数实体封装,验证不通过抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResp<?> handleBindException(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        StringJoiner stringJoiner = new StringJoiner(",");
        for (ObjectError error : bindingResult.getAllErrors()) {
            stringJoiner.add(error.getDefaultMessage());
        }
        log.error("框架捕获到异常:[{}][{}]", ResultStatus.http_status_method_param_error.getErrorCode(), stringJoiner);
        return  ResultUtil.common(ResultStatus.http_status_method_param_error.getErrorCode(), stringJoiner.toString());
    }

    /**
     * 这里是处理 @PathVariable和@RequestParam 验证不通过抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResp<?> handle(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringJoiner stringJoiner = new StringJoiner(",");
        for (ConstraintViolation<?> item : violations) {
            stringJoiner.add(item.getMessage());
        }
        log.error("框架捕获到异常:[{}][{}]", ResultStatus.http_status_method_param_error.getErrorCode(), stringJoiner);
        return ResultUtil.common(ResultStatus.http_status_method_param_error.getErrorCode(), stringJoiner.toString());
    }

    /**
     * 这里处理@RequestBody,验证不通过抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResp<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringJoiner stringJoiner = new StringJoiner(",");
        // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            stringJoiner.add(error.getDefaultMessage());
        }
        log.error("框架捕获到异常:[{}][{}]", ResultStatus.http_status_method_param_error.getErrorCode(), stringJoiner);
        return ResultUtil.common(ResultStatus.http_status_method_param_error.getErrorCode(), stringJoiner.toString());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public BaseResp<?> business(CustomException ex) {
        log.error("框架捕获到异常:[{}][{}]", ex.getCode(), ex.getMsg());
        return ResultUtil.common(ex.getCode(), ex.getMsg());
    }
}
