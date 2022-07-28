package com.niulijie.mdm.config;


import com.alibaba.excel.exception.ExcelAnalysisException;
import com.niulijie.mdm.result.BaseResult;
import com.niulijie.mdm.result.BusinessException;
import com.niulijie.mdm.result.Result;
import com.niulijie.mdm.result.ResultUtil;
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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.StringJoiner;

/**
 * 全局参数错误异常返回
 * @author df
 * @date 2019/9/16
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 系统异常处理，比如：404,500
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResult defaultErrorHandler(Exception e) {
        BaseResult BaseResult = new BaseResult();
        if (e instanceof HttpMessageNotReadableException) {
            BaseResult.setCode(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode());
            BaseResult.setMsg(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getMsg());
        } else if (e instanceof ExcelAnalysisException) {
            BaseResult.setMsg(e.getMessage());
            BaseResult.setCode(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode());
        } else {
            BaseResult.setMsg(Result.ERROR.getMsg());
            BaseResult.setCode(Result.ERROR.getCode());
        }
        e.printStackTrace();
        log.error("系统出现未知错误，错误信息为：" + e);
        logError(e);
        BaseResult.setData("");
        BaseResult.setTime(LocalDateTime.now());
        return BaseResult;
    }

    /**
     * 这里处理Get请求多参数实体封装,验证不通过抛出的异常
     */
    @ExceptionHandler(BindException.class)
    public BaseResult handleBindException(BindException e){
        BindingResult bindingResult = e.getBindingResult();
        StringJoiner stringJoiner = new StringJoiner(",");
        for (ObjectError error : bindingResult.getAllErrors()) {
            stringJoiner.add(error.getDefaultMessage());
        }
        log.error("框架捕获到异常:[{}][{}]", Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), stringJoiner);
        return  ResultUtil.common(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), stringJoiner.toString());
    }

    /**
     * 这里是处理 @PathVariable和@RequestParam 验证不通过抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult handle(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringJoiner stringJoiner = new StringJoiner(",");
        for (ConstraintViolation item : violations) {
            stringJoiner.add(item.getMessage());
        }
        log.error("框架捕获到异常:[{}][{}]", Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), stringJoiner);
        return ResultUtil.common(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), stringJoiner.toString());
    }

    /**
     * 这里处理@RequestBody,验证不通过抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringJoiner stringJoiner = new StringJoiner(",");
        // 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            stringJoiner.add(error.getDefaultMessage());
        }
        log.error("框架捕获到异常:[{}][{}]", Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), stringJoiner);
        return ResultUtil.common(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), stringJoiner.toString());
    }

    /**
     * 这里处理上传文件大小验证
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResult handlerMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResultUtil.common(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), "上传文件大小不能超过100M");
    }

    /**
     * 这里处理校验不通过抛出的异常
     */
    @ExceptionHandler(ValidationException.class)
    public BaseResult handleValidationException(ValidationException e){
        return  ResultUtil.common(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResult business(BusinessException ex) {
        log.error("框架捕获到异常:[{}][{}]", ex.getCode(), ex.getMessage());
        logError(ex);
        return ResultUtil.common(ex.getCode(), ex.getMessage());
    }

    /**
     * 记录异常详细日志
     *
     * @param ex
     */
    public static void logError(Exception ex) {
        StringBuilder msg = new StringBuilder(ex.getMessage() == null ? "null" : ex.getMessage()).append("\r\n");
        StackTraceElement[] traces = ex.getStackTrace();
        for (StackTraceElement trace : traces) {
            msg.append("\tat ").append(trace).append("\r\n");
        }
        log.error(msg.toString());
    }
}
