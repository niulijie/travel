package com.niulijie.springboot.entity;

import lombok.*;

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
@ToString
@EqualsAndHashCode
public class Demo {

    private Integer id;
    private String name;

    private UserTest user;
}
