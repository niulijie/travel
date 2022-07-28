package com.niulijie.mdm.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMqConfig {

    /**
     * 用户中心 连接工厂
     */
    @Bean(name = "ucenterConnectionFactory")
    @Primary
    public ConnectionFactory ucenterConnectionFactory(@Value("${spring.rabbitmq.host}") String host,
                                                      @Value("${spring.rabbitmq.port}") int port,
                                                      @Value("${spring.rabbitmq.username}") String username,
                                                      @Value("${spring.rabbitmq.password}") String password,
                                                      @Value("${spring.rabbitmq.virtual-host}") String vHost) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vHost);
        return connectionFactory;
    }

    /**
     * 用户中心 消费者
     */
    @Bean(name = "ucenterListenerFactory")
    public SimpleRabbitListenerContainerFactory ucenterListenerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("ucenterConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        //设置手动ack
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(listenerContainerFactory, connectionFactory);
        return listenerContainerFactory;
    }

    @Bean(name = "ucenterRabbitAdmin")
    public RabbitAdmin ucenterRabbitAdmin(
            @Qualifier("ucenterConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

}