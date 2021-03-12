package com.niulijie.springboot.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 不在Spring Boot的扫描包下方式一
 * 实现接口：ApplicationContextAware
 * 通过启动类里面的@Bean标签给applicationContext赋值
 * @author edz
 */
public class SpringContextUtils2 implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextUtils2.applicationContext == null){
            SpringContextUtils2.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("---------------com.niulijie.springboot.util.SpringContextUtils2赋值---------------------------------");
        System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringContextUtils2.getAppContext()获取applicationContext对象,applicationContext="+SpringContextUtils2.applicationContext+"========");
        System.out.println("---------------------------------------------------------------------");
    }
    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz 类型
     * @param <T> 泛型
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
