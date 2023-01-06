package com.niulijie.ucenter.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.niulijie.ucenter.constant.CodeConstant;
import com.niulijie.ucenter.constant.CodeNameConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class BaseResp<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态描述
     */
    private String message;

    /**
     * 响应消息体(泛型)
     */
    private T data = null;

    /**
     * 构造方法
     */
    public BaseResp() {
    }

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param args 错误参数
     */
    public BaseResp(int code, Object... args) {
        this.code = code;
        if (!ArrayUtils.isEmpty(args)) {
            this.message = CodeNameConstant.getMsg(code);
        }
    }

    /**
     * 构造方法
     *
     * @param code 状态码
     * @param msg  状态描述
     */
    public BaseResp(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    /**
     * 不带数据的返回结果
     * @param resultStatus
     */
    public BaseResp(ResultStatus resultStatus) {
        this.code = resultStatus.getErrorCode();
        this.message = resultStatus.getErrorMsg();
        this.data = data;
    }

    /**
     * 构造方法
     *
     */
    public BaseResp(ResultStatus resultStatus, T data) {
        this.code = resultStatus.getErrorCode();
        this.message = resultStatus.getErrorMsg();
        this.data = data;
    }

    /**
     * 构造方法
     *
     * @param code    状态码
     * @param message 状态描述
     * @param data    响应消息体(泛型)
     */
    public BaseResp(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    /**
     * 静态构造方法，处理成功
     */
    public static <T> BaseResp<T> success() {
        return new BaseResp<>(CodeConstant.SUCCESS);
    }

    /**
     * 静态构造方法，处理成功
     *
     * @param message 前端提示信息
     */
    public static <T> BaseResp<T> success(String message) {
        return new BaseResp<>(CodeConstant.SUCCESS, message);
    }

    /**
     * 静态构造方法，处理成功
     *
     * @param body 响应消息体(泛型)
     */
    public static <T> BaseResp<T> success(T body) {
        BaseResp<T> restResponse = new BaseResp<>(CodeConstant.SUCCESS);
        restResponse.setData(body);
        return restResponse;
    }

    /**
     * 静态构造方法，处理成功
     *
     * @param body 响应消息体(泛型)
     */
    public static <T> BaseResp<T> successBody(T body) {
        BaseResp<T> restResponse = new BaseResp<>(CodeConstant.SUCCESS);
        restResponse.setData(body);
        return restResponse;
    }

    public static <T> BaseResp<T> create(int errorCode, String msg, T data) {
        BaseResp<T> baseResp = new BaseResp<>();
        baseResp.setCode(errorCode);
        baseResp.setData(data);
        baseResp.setMessage(msg);
        return baseResp;
    }

    /**
     * 判断response是正确的
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return Objects.equals(this.code, CodeConstant.SUCCESS);
    }

    /**
     * 判断response是错误的
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public boolean isFailure() {
        return !isSuccess();
    }



    @Data
    public static class Trace  {

        /**
         * 二级错误状态描述
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String subMsg;

        /**
         * 异常类型
         */
        private String errType;

        /**
         * 异常堆栈信息
         */
        private String errTrace;

        /**
         * 调用跟踪栈元素
         */
        private StackTraceElement stackTraceElements;
    }
}
