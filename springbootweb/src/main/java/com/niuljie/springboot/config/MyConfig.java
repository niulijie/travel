package com.niuljie.springboot.config;

import com.niuljie.springboot.dto.Color;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class MyConfig {

    @Profile("sec")
    @Bean
    public Color red(){
        return new Color();
    }

    @Profile("dev")
    @Bean
    public Color green(){
        return new Color();
    }
}
