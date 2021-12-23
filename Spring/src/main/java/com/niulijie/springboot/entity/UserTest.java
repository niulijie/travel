package com.niulijie.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.niulijie.springboot.enums.AgeEnum;
import com.niulijie.springboot.enums.GenderEnum;
import com.niulijie.springboot.enums.GradeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author niulijie
 * @since 2021-12-21 16:56:31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("user_test")
public class UserTest implements Serializable {

    private static final long serialVersionUID = 165531383239264303L;

    /**
     * 雪花算法
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userid;

    private String name;

    /**
     * IEnum接口的枚举处理
     */
    private AgeEnum age;

    /**
     * 原生枚举（带{@link com.baomidou.mybatisplus.annotation.EnumValue}):
     * 数据库的值对应该注解对应的属性
     */
    private GradeEnum grade;

    /**
     * 原生枚举： 默认使用枚举值顺序： 0：MALE， 1：FEMALE
     */
    private GenderEnum sex;

}
