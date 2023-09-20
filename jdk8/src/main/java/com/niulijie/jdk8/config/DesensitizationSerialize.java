package com.niulijie.jdk8.config;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.niulijie.jdk8.enums.DesensitizationTypeEnum;
import com.niulijie.jdk8.annotation.Desensitization;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * 自定义的序列化类
 * @author niuli
 */
@AllArgsConstructor
@NoArgsConstructor
public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizationTypeEnum typeEnum;

    private Integer startInclude;

    private Integer endExclude;

    /**
     * Method that can be called to ask implementation to serialize
     * values of type this serializer handles.
     *
     * @param value       Value to serialize; can <b>not</b> be null.
     * @param gen         Generator used to output resulting Json content
     * @param serializers Provider that can be used to get serializers for
     */
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        switch (typeEnum) {
            // 自定义类型脱敏
            case MY_RULE:
                gen.writeString(CharSequenceUtil.hide(value, startInclude, endExclude));
                break;
            // userId脱敏
            case USER_ID:
                gen.writeString(String.valueOf(DesensitizedUtil.userId()));
                break;
            // 中文姓名脱敏
            case CHINESE_NAME:
                gen.writeString(String.valueOf(DesensitizedUtil.chineseName(value)));
                break;
            // 身份证脱敏
            case ID_CARD:
                gen.writeString(DesensitizedUtil.idCardNum(String.valueOf(value), 1, 2));
                break;
            // 固定电话脱敏
            case FIXED_PHONE:
                gen.writeString(DesensitizedUtil.fixedPhone(value));
                break;
            // 手机号脱敏
            case MOBILE_PHONE:
                gen.writeString(DesensitizedUtil.mobilePhone(value));
                break;
            // 地址脱敏
            case ADDRESS:
                gen.writeString(DesensitizedUtil.address(value, 8));
                break;
            // 邮箱脱敏
            case EMAIL:
                gen.writeString(DesensitizedUtil.email(value));
                break;
            // 密码脱敏
            case PASSWORD:
                gen.writeString(DesensitizedUtil.password(value));
                break;
            // 中国车牌脱敏
            case CAR_LICENSE:
                gen.writeString(DesensitizedUtil.carLicense(value));
                break;
            // 银行卡脱敏
            case BANK_CARD:
                gen.writeString(DesensitizedUtil.bankCard(value));
                break;
            default:
        }
    }

    /**
     * Method called to see if a different (or differently configured) serializer
     * is needed to serialize values of specified property.
     * Note that instance that this method is called on is typically shared one and
     * as a result method should <b>NOT</b> modify this instance but rather construct
     * and return a new instance. This instance should only be returned as-is, in case
     * it is already suitable for use.
     *
     * @param prov     Serializer provider to use for accessing config, other serializers
     * @param property Method or field that represents the property
     *                 (and is used to access value to serialize).
     *                 Should be available; but there may be cases where caller cannot provide it and
     *                 null is passed instead (in which case impls usually pass 'this' serializer as is)
     * @return Serializer to use for serializing values of specified property;
     * may be this instance or a new instance.
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        if (property != null){
            //判断数据类型是否为String类型
            if (Objects.equals(property.getType().getRawClass(), String.class)){
                // 获取定义的注解
                Desensitization desensitization = property.getAnnotation(Desensitization.class);
                // 为null
                if (desensitization == null){
                    desensitization = property.getContextAnnotation(Desensitization.class);
                }
                if (desensitization != null){
                    // 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。
                    return new DesensitizationSerialize(desensitization.type(), desensitization.startInclude(),
                            desensitization.endExclude());
                }
            }
            return prov.findValueSerializer(property.getType(), property);
        }
        return prov.findNullValueSerializer(null);
    }
}
