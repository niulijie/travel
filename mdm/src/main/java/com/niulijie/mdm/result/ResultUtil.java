package com.niulijie.mdm.result;


import lombok.experimental.UtilityClass;

@UtilityClass
public class ResultUtil {

    public BaseResult ok(String msg, Object data) {
        return BaseResult.create(Result.SUCCESS.getCode(), msg, data);
    }

    public BaseResult ok(Result result, Object data) {
        return BaseResult.create(result.getCode(), result.getMsg(), data);
    }

    public BaseResult ok(Object data) {
        return ok(Result.SUCCESS.getMsg(), data);
    }

    public BaseResult ok() {
        return ok(null);
    }

    public BaseResult error() {
        return BaseResult.create(Result.ERROR.getCode(), Result.ERROR.getMsg(), null);
    }

    public BaseResult error(Integer code, String msg) {
        return BaseResult.create(code, msg, null);
    }

    public BaseResult error(String msg) {
        return BaseResult.create(Result.ERROR.getCode(), msg, null);
    }

    public BaseResult error(Result result, String msg) {
        return BaseResult.create(result.getCode(), msg, null);
    }

    public BaseResult common(Result result, Object data) {
        return BaseResult.create(result.getCode(), result.getMsg(), data);
    }

    public BaseResult common(Result result) {
        return BaseResult.create(result.getCode(), result.getMsg(), null);
    }

    public BaseResult common(Integer code, String msg) {
        return BaseResult.create(code, msg, null);
    }

    public BaseResult common(Integer code, String msg, Object data) {
        return BaseResult.create(code, msg, data);
    }
}
