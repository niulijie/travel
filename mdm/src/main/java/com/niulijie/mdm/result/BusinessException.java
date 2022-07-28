package com.niulijie.mdm.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dufa
 *  业务异常
 * @date  2020年12月09日
 */

@Setter
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -4435979898433185113L;

    /**
     * 错误码
     */
    private int code;


    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Result result) {
        super(result.getMsg());
        this.code = result.getCode();
    }

}
