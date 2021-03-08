package com.niulijie.springboot.test;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 主配置类  包扫描
 * @author edz
 */
@ComponentScan(value="com.niulijie.springboot")
@Configuration
public class MainScanConfig {

}
