package com.niulijie.mdm.dto.enums;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.jwportal.alk.vmc.video.common
 * @email zhoupengbing@telecomyt.com.cn
 * @description 数据行为枚举类
 * @createTime 2020年05月31日 09:33:00
 * @Version v1.0
 */
public enum ActionTypeEnum {

    ADD(1),
    UPDATE(2),
    DELETE(3),;

    private Integer code;

    ActionTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }

    public static <T extends ActionTypeEnum>T getEnumByType(Integer code,Class<T> enumClass){
        for(T t:enumClass.getEnumConstants()){
            if(t.getCode().equals(code)){
                return t;
            }
        }
        return null;
    }

}
