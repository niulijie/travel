package com.niulijie.springboot.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "person")
public class Person {
    private String userName;
    private Boolean boss;
    private Date birth;
    private Integer age;
    private Demo demo;
    private String[] interests;
    private String[] bodies;
    private List<String> animals;
    private List<String> relations;
    private Map<String,Object> scores;
    private Map<String,Object> numbers;
    private Set<Double> salaries;
    private Set<String> foods;
    private Map<String, List<Demo>> allDemos;
}
