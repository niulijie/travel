package com.niulijie.mdm.constant;

/**
 * 受限类型（黑名单）枚举
 * @author niuli
 */

public enum LimitTypeEnum {

    LIMIT_TYPE_NO(0, "不受限"),
    LIMIT_TYPE_FORBID_VIDEO(1, "发布视频"),
    LIMIT_TYPE_FORBID_COMMENT(2, "发布评论回复"),
    LIMIT_TYPE_FORBID_FIND(3, "不能发现");

    private Integer code;

    private String name;

    LimitTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 获取代码对应的值
     * @param code 枚举代码
     * @return
     */
    public static String getName(int code) {
        for (LimitTypeEnum opTypeEnum : values()) {
            if (opTypeEnum.getCode() == code) {
                return opTypeEnum.getName();
            }
        }
        return null;
    }
}
