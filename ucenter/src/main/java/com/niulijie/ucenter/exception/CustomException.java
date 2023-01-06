package com.niulijie.ucenter.exception;

import com.niulijie.ucenter.common.BaseResp;
import com.niulijie.ucenter.common.ResultStatus;
import com.niulijie.ucenter.constant.CodeNameConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 自定义异常类
 */
@Getter
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected Integer errorCode;
    @Setter
    @Accessors(chain = true)
    private Object body;

    /**
     * 是否报警
     */
    private boolean alarm;

    /**
     * @param code 错误码，
     */
    public CustomException(int code) {
        super(CodeNameConstant.getMsg(code));
        this.errorCode = code;
    }

    /**
     * @param code 错误码
     */
    public CustomException(int code, Object[] args) {
        super(CodeNameConstant.getMsg(code));
        this.errorCode = code;
    }

    public CustomException(int code, String message) {
        super(message);
        this.errorCode = code;
    }

    public CustomException(BaseResp baseResp) {
        super(baseResp.getMessage());
        this.errorCode = baseResp.getCode();
    }

    /*public CustomException(AcGroupConstant acGroupConstant) {
        super(acGroupConstant.getMsg());
        this.errorCode = acGroupConstant.getCode();
    }

    public CustomException(AttrConstant attrConstant) {
        super(attrConstant.getMsg());
        this.errorCode = attrConstant.getCode();
    }*/

    /**
     * 继承exception，加入错误状态值
     * @param resultStatus
     */
    public CustomException(ResultStatus resultStatus) {
        this.errorCode = resultStatus.getErrorCode();
    }

    public CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = code;
    }

    public CustomException alarm() {
        this.alarm = true;
        return this;
    }

    public int getCode() {
        return errorCode;
    }

    public boolean isAlarm() {
        return alarm;
    }
}
