package com.niuljie.springboot.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("sec")
@Data
@Component
@ConfigurationProperties("human")
public class Boss implements Human{
    private String name;
    private Integer age;
}
