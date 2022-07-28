package com.niulijie.mdm.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 返回结果封装工具类
 *
 * @author df
 * @date 2019/8/6
 */
@Setter
@Getter
public class BaseResult implements Serializable {

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回信息描述
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    /**
     * 时间
     */
    private LocalDateTime time;


    public BaseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
        this.time = LocalDateTime.now();
    }

    public BaseResult(int code, Object data) {
        this.code = code;
        this.msg = Result.SUCCESS.getMsg();
        this.data = data;
        this.time = LocalDateTime.now();
    }

    public BaseResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.time = LocalDateTime.now();
    }

    public BaseResult(Object data) {
        this.code = Result.SUCCESS.getCode();
        this.msg =  Result.SUCCESS.getMsg();
        this.data = data;
        this.time = LocalDateTime.now();
    }

    public BaseResult() {

    }

    public static BaseResult create(int code, String msg, Object data) {
        return new BaseResult(code, msg, data);
    }

    public static BaseResult create(int code, Object data) {
        return new BaseResult(code, Result.SUCCESS.getMsg(), data);
    }

}
