package com.niulijie.springboot.controller;

import com.niulijie.springboot.util.SpringContextUtils2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages={"com.niulijie.springboot"}) // 扫描该包路径下的所有spring组件
/*@EnableJpaRepositories("com.niulijie.springboot.dao") // JPA扫描该包路径下的Repositorie
 *//*@EntityScan("com.niulijie.springboot.entity") // 扫描实体类
 */
//@SpringBootApplication
//@EnableScheduling
/**
 * 通过启动类里面的@Bean标签给applicationContext赋值
 * 必须和启动类一起才可以
 */

@Import(SpringContextUtils2.class)
public class App2 extends SpringBootServletInitializer {

    /*public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }*/
}
