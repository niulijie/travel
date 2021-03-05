package com.niulijie.springboot;

//@EnableAutoConfiguration//启用springboot自动配置

import com.niulijie.springboot.test.MainScanConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 1.定义扫描的路径从中找出标识了需要装配的类自动装配到spring的bean容器中,类似于<context:component-scan base-package="">
 *     @ComponentScan注解默认就会装配标识了@Controller，@Service，@Repository，@Component注解的类到spring容器中
 *
 */
//@ComponentScan
@SpringBootApplication
public class SpringBootTestApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootTestApplication.class, args);
        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
        for (String beanName: beanDefinitionNames) {
            System.out.println(beanName);
        }
        MainScanConfig bean = configurableApplicationContext.getBean(MainScanConfig.class);
    }
}
