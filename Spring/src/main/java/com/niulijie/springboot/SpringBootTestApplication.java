package com.niulijie.springboot;

import ch.qos.logback.core.db.DBHelper;
import com.niulijie.springboot.config.BeanConfig;
import com.niulijie.springboot.entity.Demo;
import com.niulijie.springboot.entity.UserTest;
import com.niulijie.springboot.test.MainScanConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ---------------------------@SpringBootApplication---------------------------------------------------------------------------------
 * 1.@SpringBootApplication(scanBasePackages = "com.niulijie.springboot")这个注解, 它是由下面主要的注解共同组成的复合的注解
 * 2.相当于 （三个注解起了主导作用）
 *   @SpringBootConfiguration 代表是springboot的一个配置类
 *   @EnableAutoConfiguration 启用springboot自动配置，开启spring+mvc的自动化配置
 *   @ComponentScan("com.niulijie.springboot") 指定包扫描，确保可以扫描到启动类父包下的所有类
 *
 * ---------------------------@EnableAutoConfiguration---------------------------------------------------------------------------------
 * 1.@AutoConfigurationPackage 自动配置包-指定默认包规则
 *   里面是@Import(AutoConfigurationPackages.Registrar.class)，给容器中导入Registrar组件，利用Registrar给容器中导入一系列组件
 *   AnnotationMetadata:注解源信息(指注解标在了哪里introspectedClass->SpringBootTestApplication，属性值是什么)，注解是指AutoConfigurationPackages
 *   new PackageImport(metadata).getPackageName()-> com.niulijie.springboot:把这个包下的所有组件注册进容器中
 *
 * 2.@Import(AutoConfigurationImportSelector.class)
 *   利用getAutoConfigurationEntry(annotationMetadata);给容器中批量导入一些组件
 *   List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);获取到需要导入容器中必须的127个自动配置类
 *   List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),getBeanClassLoader());
 *     --> Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader)利用工厂的名字加载容器
 *      -->从META-INF/spring.factories位置来加载一个文件，默认扫描当前系统里面所有META-INF/spring.factories位置的文件
 *      (D:\repository\org\springframework\boot\spring-boot-autoconfigure\2.2.7.RELEASE\spring-boot-autoconfigure-2.2.7.RELEASE.jar!\META-INF\spring.factories)
 *      文件中写死了springboot一启动需要在容器中加载的所有配置类
 * 3.虽然127个自动配置类会默认加载，但是会按照条件装配规则(@Conditional)，按需配置
 *
 * ----------------------------@ComponentScan-------------------------------------------------------------------------------------------
 *  1.定义扫描的路径从中找出标识了需要装配的类自动装配到spring的bean容器中,类似于<context:component-scan base-package="">
 *   @ComponentScan注解默认就会装配标识了@Controller，@Service，@Repository，@Component注解的类到spring容器中
 *      @Controller，@Service，@Repository注解，有一个共同的注解@Component
 *  2.通过includeFilters加入扫描路径下没有以上注解的类加入spring容器
 *  3.通过excludeFilters过滤出不用加入spring容器的类
 *  4.自定义增加了@Component注解的注解方式
 *
 *  ----------------------------总结-------------------------------------------------------------------------------------------
 *  1.SpringBoot先加载所有的自动配置类   xxxxAutoConfiguration
 *  2.每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值  xxxxProperties里面拿值，xxxxProperties和配置文件进行绑定
 *  3.生效的配置类会给容器中装配很多组件
 *  4.只要容器中有这些组件，相当于这些功能就有了
 *  5.定制化配置
 *      用户直接自己用@Bean替换底层的组件
 *      用户去看这个组件是获取的配置文件值进行修改
 *  xxxxAutoConfiguration --> 组件 --> xxxxProperties里面拿值 --> application.properties
 * ----------------------------扩展-------------------------------------------------------------------------------------------
 * 配置类只有一个有参构造器，有参构造器所有参数的值都会从容器中确定
  */
@SpringBootApplication
@MapperScan("com.niulijie.*.mapper")
public class SpringBootTestApplication {
    public static void main(String[] args) {
        //1.返回IOC容器-run方法的返回值ConfigurableApplicationContext继承了ApplicationContext上下文接口
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootTestApplication.class, args);
        //2.查看容器里面得组件
        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
        for (String beanName: beanDefinitionNames) {
            System.out.println(beanName);
        }
        //3.从容器中获取组件
//        Demo testDemo = configurableApplicationContext.getBean("testDemo", Demo.class);
//        System.out.println("****testDemo:"+testDemo);
//        UserTest userTest = configurableApplicationContext.getBean("user02", UserTest.class);
//        //@Configuration(proxyBeanMethods = true)--Demo中的userTest和容器中的UserTest是否相同：true
//        //@Configuration(proxyBeanMethods = false)--Demo中的userTest和容器中的UserTest是否相同：false
//        System.out.println("Demo中的userTest和容器中的UserTest是否相同："+ (testDemo.getUser() == userTest));
//
//        //4.springBGLB生成的
//        BeanConfig bean = configurableApplicationContext.getBean(BeanConfig.class);
//        UserTest user02 = bean.user02();
//        UserTest user01 = bean.user02();
//        //true--@Configuration(proxyBeanMethods = true),获取的是代理对象调用方法，SpringBoot总会检查这个组件是否在容器中，保持组件单实例
//        //false--@Configuration(proxyBeanMethods = false)
//        System.out.println(user01 == user02);
//
//        //5.获取组件
//        /*
//            com.niulijie.springboot.entity.UserTest --  @Import({UserTest.class, DBHelper.class})导入
//            user02 -- @Bean 添加进去的
//         */
//        String[] beanNamesForType = configurableApplicationContext.getBeanNamesForType(UserTest.class);
//        System.out.println("###########################");
//        for (String s : beanNamesForType) {
//            System.out.println(s);
//        }
//
//        //ch.qos.logback.core.db.DBHelper@2e4389ed
//        DBHelper bean1 = configurableApplicationContext.getBean(DBHelper.class);
//        System.out.println(bean1);

        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor
         * org.springframework.context.annotation.internalCommonAnnotationProcessor
         * org.springframework.context.event.internalEventListenerProcessor
         * org.springframework.context.event.internalEventListenerFactory
         * springBootTestApplication
         * org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory
         * userController =====
         * userDao =====
         * userService =====
         * mainScanConfig =====
         * org.springframework.boot.autoconfigure.AutoConfigurationPackages
         * org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration
         * propertySourcesPlaceholderConfigurer
         * org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration$TomcatWebSocketConfiguration
         * websocketServletWebServerCustomizer
         * org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
         * org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryConfiguration$EmbeddedTomcat
         * tomcatServletWebServerFactory
         * org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration
         * servletWebServerFactoryCustomizer
         * tomcatServletWebServerFactoryCustomizer
         * org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor
         * org.springframework.boot.context.internalConfigurationPropertiesBinderFactory
         * org.springframework.boot.context.internalConfigurationPropertiesBinder
         * org.springframework.boot.context.properties.ConfigurationPropertiesBeanDefinitionValidator
         * org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata
         * server-org.springframework.boot.autoconfigure.web.ServerProperties
         * webServerFactoryCustomizerBeanPostProcessor
         * errorPageRegistrarBeanPostProcessor
         * org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration$DispatcherServletConfiguration
         * dispatcherServlet
         * spring.mvc-org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties
         * spring.http-org.springframework.boot.autoconfigure.http.HttpProperties
         * org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration$DispatcherServletRegistrationConfiguration
         * dispatcherServletRegistration
         * org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration
         * org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
         * taskExecutorBuilder
         * applicationTaskExecutor
         * spring.task.execution-org.springframework.boot.autoconfigure.task.TaskExecutionProperties
         * org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration
         * defaultValidator
         * methodValidationPostProcessor
         * org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration$WhitelabelErrorViewConfiguration
         * error
         * beanNameViewResolver
         * org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration$DefaultErrorViewResolverConfiguration
         * conventionErrorViewResolver
         * org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
         * errorAttributes
         * basicErrorController
         * errorPageCustomizer
         * preserveErrorControllerTargetClassPostProcessor
         * spring.resources-org.springframework.boot.autoconfigure.web.ResourceProperties
         * org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration
         * requestMappingHandlerAdapter
         * requestMappingHandlerMapping
         * welcomePageHandlerMapping
         * mvcConversionService
         * mvcValidator
         * mvcContentNegotiationManager
         * mvcPathMatcher
         * mvcUrlPathHelper
         * viewControllerHandlerMapping
         * beanNameHandlerMapping
         * routerFunctionMapping
         * resourceHandlerMapping
         * mvcResourceUrlProvider
         * defaultServletHandlerMapping
         * handlerFunctionAdapter
         * mvcUriComponentsContributor
         * httpRequestHandlerAdapter
         * simpleControllerHandlerAdapter
         * handlerExceptionResolver
         * mvcViewResolver
         * mvcHandlerMappingIntrospector
         * org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter
         * defaultViewResolver
         * viewResolver
         * requestContextFilter
         * org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
         * formContentFilter
         * org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration
         * mbeanExporter
         * objectNamingStrategy
         * mbeanServer
         * org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration
         * springApplicationAdminRegistrar
         * org.springframework.boot.autoconfigure.aop.AopAutoConfiguration$ClassProxyingConfiguration
         * org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
         * org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
         * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$Jackson2ObjectMapperBuilderCustomizerConfiguration
         * standardJacksonObjectMapperBuilderCustomizer
         * spring.jackson-org.springframework.boot.autoconfigure.jackson.JacksonProperties
         * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$JacksonObjectMapperBuilderConfiguration
         * jacksonObjectMapperBuilder
         * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$ParameterNamesModuleConfiguration
         * parameterNamesModule
         * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration$JacksonObjectMapperConfiguration
         * jacksonObjectMapper
         * org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
         * jsonComponentModule
         * org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration$StringHttpMessageConverterConfiguration
         * stringHttpMessageConverter
         * org.springframework.boot.autoconfigure.http.JacksonHttpMessageConvertersConfiguration$MappingJackson2HttpMessageConverterConfiguration
         * mappingJackson2HttpMessageConverter
         * org.springframework.boot.autoconfigure.http.JacksonHttpMessageConvertersConfiguration
         * org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
         * messageConverters
         * org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration
         * spring.info-org.springframework.boot.autoconfigure.info.ProjectInfoProperties
         * org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration
         * spring.security.oauth2.resourceserver-org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
         * org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
         * taskSchedulerBuilder
         * spring.task.scheduling-org.springframework.boot.autoconfigure.task.TaskSchedulingProperties
         * org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
         * restTemplateBuilder
         * org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration$TomcatWebServerFactoryCustomizerConfiguration
         * tomcatWebServerFactoryCustomizer
         * org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
         * org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration
         * characterEncodingFilter
         * localeCharsetMappingsCustomizer
         * org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration
         * multipartConfigElement
         * multipartResolver
         * spring.servlet.multipart-org.springframework.boot.autoconfigure.web.servlet.MultipartProperties
         * org.springframework.aop.config.internalAutoProxyCreator
         */

        /*
            6.测试@ConditionalOnBean(name = "niuda")
                容器中niuda组件：false
                容器中testDemo组件：false
            ------------无注解时-----------------
                容器中niuda组件：false
                容器中testDemo组件：true
         */
        boolean niuda = configurableApplicationContext.containsBean("niuda");
        System.out.println("容器中niuda组件："+ niuda);

        boolean testDemo = configurableApplicationContext.containsBean("testDemo");
        System.out.println("容器中testDemo组件："+testDemo);

        //@ImportResource("classpath:beans.xml") -- 容器中user01组件：true
        boolean user01 = configurableApplicationContext.containsBean("user01");
        System.out.println("容器中user01组件："+user01);
    }
}
