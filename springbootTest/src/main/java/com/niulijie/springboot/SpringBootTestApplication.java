package com.niulijie.springboot;

//@EnableAutoConfiguration//启用springboot自动配置

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1.定义扫描的路径从中找出标识了需要装配的类自动装配到spring的bean容器中,类似于<context:component-scan base-package="">
 *     @ComponentScan注解默认就会装配标识了@Controller，@Service，@Repository，@Component注解的类到spring容器中
 *
 */
//@ComponentScan
@SpringBootApplication
public class SpringBootTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApplication.class,args);
    }
}
