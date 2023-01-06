package com.niulijie.ucenter.exception;

import com.niulijie.ucenter.menus.ErrorEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author byron
 * @date 2021.7.15
 */
@Getter
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code = -1;

    @Setter
    @Accessors(chain = true)
    private Object body;

    public CommonException(ErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();
    }

    public CommonException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public CommonException(String msg) {
        super(msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
