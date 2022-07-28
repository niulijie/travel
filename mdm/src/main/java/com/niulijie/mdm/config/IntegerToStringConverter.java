package com.niulijie.mdm.config;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;

/**
 * 超过10000得序列化为字符串,注解
 * 属性添加@JsonDeserialize(converter = IntegerToStringConverter.class)
 */
public class IntegerToStringConverter extends StdConverter<Integer, Object> {

    @Override
    public Object convert(Integer value) {
        if (value >= 10000) {
            float v = value.floatValue();
            float s = v / 10000;
            float result = BigDecimal.valueOf(s).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            return result + "w";
        }
        return value;
    }

}
