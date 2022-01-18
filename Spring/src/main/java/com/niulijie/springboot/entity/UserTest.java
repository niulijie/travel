package com.niulijie.springboot.entity;

import com.alibaba.excel.util.DateUtils;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.niulijie.springboot.enums.AgeEnum;
import com.niulijie.springboot.enums.GenderEnum;
import com.niulijie.springboot.enums.GradeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 雪花算法--自动生成主键
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

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

    /**
     * 创建时间  @TableField(fill = FieldFill.INSERT) --自动填充功能
     */
    @TableField(fill = FieldFill.INSERT,value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;
}
