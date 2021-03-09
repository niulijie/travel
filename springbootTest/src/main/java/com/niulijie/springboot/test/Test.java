package com.niulijie.springboot.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext2 = new AnnotationConfigApplicationContext(MainScanConfig.class);
        String[] definitionNames = applicationContext2.getBeanDefinitionNames();
        for (String name : definitionNames) {
            System.out.println(name);
        }
    }
}
