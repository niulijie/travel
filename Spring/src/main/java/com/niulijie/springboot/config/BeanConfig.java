package com.niulijie.springboot.config;

import ch.qos.logback.core.db.DBHelper;
import com.niulijie.springboot.entity.Car;
import com.niulijie.springboot.entity.Demo;
import com.niulijie.springboot.entity.UserTest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * 1.配置类里面使用@Bean标注在方法上给容器注册组件，默认是单实例的
 * 2.配置类本身也是一个组件
 * 3.proxyBeanMethods：代理bean的方法，默认为true
 * 4.组件依赖问题
 * Full全量级配置(proxyBeanMethods = true)；配置类组件直接有依赖关系，方法会被调用得到之前单实例组件，用Full模式
 * Lite轻量级配置(proxyBeanMethods = false) ；配置类组件直接无依赖关系用Lite模式加速容器启动过程，减少判断实例是否在容器中存在
 * 在启动的时候先通过代码方式给spring容器中注入一个bean
 * -----------------------------------------------------------------------------------------------------------------
 * @Import({UserTest.class, DBHelper.class})
 * 1.给容器中自动创建出这两个组件，默认组件的名字就是全类名
 * ------条件装配，只有name = "niuda"实例存在时，注解下的实例才会被注册-----------------------------------------------------
 * @ConditionalOnBean(name = "niuda")
 * @author niulijie
 */
//@ConditionalOnBean(name = "niuda")
@ConditionalOnMissingBean(name = "niuda")
@Import({UserTest.class, DBHelper.class})
@Configuration(proxyBeanMethods = true)//告诉SpringBoot这是一个配置类  == 配置文件(beans.xml)
//@ImportResource("classpath:beans.xml") //用于导入Spring的XML配置文件，让该配置文件中定义的bean对象加载到Spring容器中。
//@EnableConfigurationProperties(Car.class)//开启car的配置绑定功能，并且把car组件自动注册进容器中
public class BeanConfig {

    /**
     * 外部无聊对配置类中的这个组件注册方法调用多少次，获取的都是之前注册容器中的单实例对象
     * @return
     */
    //@Bean //给容器中添加组件，以方法名作为组件的id，返回类型就是组建类型。返回的值就是组件在容器中的实例
    public UserTest user02(){
        return new UserTest();
    }

    //@ConditionalOnBean(name = "niuda")
    @Bean("testDemo")//自定义组件名称
    public Demo generateDemo(){
        Demo niuniu = Demo.builder().id(1).name("niuniu").build();
        //Demo组件依赖UserTest组件
        niuniu.setUser(user02());
        return niuniu;
    }
}
