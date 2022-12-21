package com.niulijie.jdk8.dto;

import com.niulijie.jdk8.annotation.Init;
import lombok.Data;

/**
 * @author niuli
 */
@Data
@Init
public class AnnotationUser {
    @Init
    public int id;
    @Init(name = "李四")
    public String name;
    @Init(age = 18)
    public int age;
    @Init(schools = {"南开", "哈佛"})
    public String[] schools;
}
