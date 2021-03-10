package com.niulijie.springboot.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 普通类调用Spring bean对象
 * 在Spring Boot可以扫描的包下
 * 在Spring Boot可以扫描的包下,实现ApplicationContextAware接口，并加入Component注解，让spring扫描到该bean
 * 通过启动类里面的run方法在@Component标签给applicationContext赋值
 * @author edz
 */
//@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;


    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such(在填充普通bean属性之后但在init回调之前调用)
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringContextUtils.applicationContext == null) {
            SpringContextUtils.applicationContext = applicationContext;
        }

        System.out.println("---------------------------------------------------------------------");

        System.out.println("---------------com.niulijie.springboot.util.SpringContextUtils------------------------------------------------------");

        System.out.println("========ApplicationContext配置成功,SpringContextUtils.getAppContext()获取applicationContext对象,applicationContext="+SpringContextUtils.applicationContext+"========");

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
