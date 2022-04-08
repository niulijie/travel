package com.niuljie.springboot.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Person {
    private String userName;
    private Integer age;
    private Date birth;
    private Pet pet;

}
