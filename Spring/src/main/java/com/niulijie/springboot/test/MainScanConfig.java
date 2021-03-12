package com.niulijie.springboot.test;


/**
 * 主配置类  包扫描
 * @author edz
 */

import com.niulijie.spring.component.config.MyFilterType;
import com.niulijie.springboot.controller.SellComponent;
import com.niulijie.springboot.service.UserService2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

/**
  @ComponentScan(value="com.niulijie.springboot")
  mainScanConfig  @Configuration
  springBootTestApplication  @SpringBootApplication
  beanConfig    @Configuration
  testApplicationContrller  @RestController
  userController  @Controller
  userDao  @Repository
  userService  @Service
  springContextUtils  @Component
  testDemo  @Configuration-->@Bean
  ================================================================================================================
  @ComponentScan(value="com.niulijie.springboot.dao",useDefaultFilters=true,basePackageClasses= UserService.class)
  只有userDao外加basePackageClasses指定的userService加入到了spring容器中
  mainScanConfig  @Configuration
  userDao
  userService
  ================================================================================================================
  @ComponentScan(value="com.niulijie.springboot",useDefaultFilters=true(或者无),includeFilters={
                  @ComponentScan.Filter(type= FilterType.ANNOTATION, classes={Controller.class}),
                  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={UserService2.class})})
  includeFilters --> 指明哪些类需要被扫描， excludeFilters -->  过滤出不加入spring容器中的类
  mainScanConfig
  springBootTestApplication
  beanConfig
  testApplicationContrller
  userController
  userDao
  userService
  userService2 ***
  springContextUtils
  org.springframework.boot.autoconfigure.AutoConfigurationPackages
  testDemo
                  ================================================================================================================
 @ComponentScan(value="com.niulijie.springboot",useDefaultFilters=false,
 includeFilters={
 @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
 @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={UserService2.class})})
 mainScanConfig
 testApplicationController
 userController
 userService2
 */
//@ComponentScan(value="com.niulijie.springboot.dao",useDefaultFilters=true,basePackageClasses= UserService.class)
//@ComponentScan(value="com.niulijie.springboot")
//@ComponentScan(value="com.niulijie.springboot",useDefaultFilters=false,
//        includeFilters={
//                @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
//                @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={UserService2.class})})
@ComponentScan(value="com.niulijie.springboot",useDefaultFilters=false,
        includeFilters={
                @ComponentScan.Filter(type= FilterType.ANNOTATION,classes={Controller.class}),
                @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,classes={UserService2.class,SellComponent.class}),
                @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyFilterType.class})})
@Configuration
public class MainScanConfig {

}
