package com.niulijie.common.handler;

import com.niulijie.common.dto.BaseResp;
import com.niulijie.common.dto.ResultStatus;
import com.niulijie.common.dto.ResultUtil;
import com.niulijie.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author zhoupengbing
 *  异常处理类
 * @date  2019年12月09日 11:02:00
 * @version v1.0.0
 */

@RestControllerAdvice
@Slf4j
public class ExceptionHandle {

    /**
     * 这里是处理 @PathVariable和@RequestParam 验证不通过抛出的异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResp<T> handle(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        StringBuilder errorInfo = new StringBuilder();
        for (ConstraintViolation<?> item : violations) {
            errorInfo.append("参数").append(item.getMessage()).append("不能为空");
        }
        log.warn("框架捕获到异常:[{}][{}]", ResultStatus.http_status_method_param_error.getErrorCode(), errorInfo.toString());
        return ResultUtil.common(ResultStatus.http_status_method_param_error.getErrorCode(), errorInfo.toString());
    }

    /**
     * 这里处理@RequestBody,验证不通过抛出的异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResp<T> handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append("参数").append(fieldError.getDefaultMessage()).append("不能为空");
        }
        log.warn("框架捕获到异常:[{}][{}]", ResultStatus.http_status_method_param_error.getErrorCode(), errorMessage.toString());
        return ResultUtil.common(ResultStatus.http_status_method_param_error.getErrorCode(), errorMessage.toString());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    public BaseResp<T> business(CustomException ex) {
        log.warn("框架捕获到异常:[{}][{}]", ex.getCode(), ex.getMessage());
        return ResultUtil.common(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResp<T> handlerNoFoundException(Exception e) {
        log.warn("框架捕获到异常:[{}][{}]", ResultStatus.http_status_not_found.getErrorCode(), "路径不存在,请检查路径是否正确");
        return ResultUtil.common(ResultStatus.http_status_not_found.getErrorCode(), "路径不存在,请检查路径是否正确");
    }

    /**
     * 其他500异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResp<T> handleException(Exception e) {
        log.warn("框架捕获到异常:[{}][{}]", ResultStatus.http_status_internal_server_error.getErrorCode(), "系统内部错误");
        return ResultUtil.error(e.toString());
    }
}
