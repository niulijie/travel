package com.niulijie.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author edz
 * @author edz
 * @author edz
 */
@Data
/**
 * 会破坏无参和有参构造方法，需要再声明
 */
@Builder
/**
 * 有参构造方法
 */
@AllArgsConstructor
/**
 * 无参构造方法
 */
@NoArgsConstructor

public class Demo {

    private Integer id;
    private String name;
}
