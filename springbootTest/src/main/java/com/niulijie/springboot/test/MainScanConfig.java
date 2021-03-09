package com.niulijie.springboot.test;


import com.niulijie.springboot.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 主配置类  包扫描
 * @author edz
 */

/**
 * @ComponentScan(value="com.niulijie.springboot.dao",useDefaultFilters=true,basePackageClasses= UserService.class)
 * mainScanConfig  @Configuration
 * userDao
 * userService
 * ================================================================================================================
 * @ComponentScan(value="com.niulijie.springboot")
 * mainScanConfig  @Configuration
 * springBootTestApplication  @SpringBootApplication
 * beanConfig    @Configuration
 * testApplicationContrller  @RestController
 * userController  @Controller
 * userDao  @Repository
 * userService  @Service
 * springContextUtils  @Component
 * testDemo  @Configuration-->@Bean
 */
//@ComponentScan(value="com.niulijie.springboot.dao",useDefaultFilters=true,basePackageClasses= UserService.class)
@ComponentScan(value="com.niulijie.springboot")
@Configuration
public class MainScanConfig {

}
