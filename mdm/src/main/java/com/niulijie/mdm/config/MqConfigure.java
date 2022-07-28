package com.niulijie.mdm.config;

import com.niulijie.mdm.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description  MQ配置 -- 发送mq时，交换机，消息队列，key绑定
 * @author niulj
 * @date 2021/12/29 16:39
 */
@Slf4j
@Configuration
public class MqConfigure {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 配置amqpTemplate
     * @return
     */
    @Bean
    public AmqpTemplate amqpTemplate(){

        //设置rabbitTemplate参数
        //jackson消息转换器
        rabbitTemplate.setMessageConverter( new Jackson2JsonMessageConverter() );
        //设置编码
        rabbitTemplate.setEncoding( "UTF-8" );
        //开启回调
        rabbitTemplate.setMandatory( true );
        //消息回调-记录信息
//        rabbitTemplate.setReturnCallback( (message, replyCode, replyText, exchange, routingKey) -> {
//            String correlationId = message.getMessageProperties().getCorrelationId();
//            log.info( "消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}",
//                      correlationId, replyCode, replyText, exchange, routingKey );
//        } );
        //消息确认
        rabbitTemplate.setConfirmCallback( (correlationData, ack, cause) -> {
            if (ack) {
                log.info( "消息发送到exchange成功" );
            } else {
                log.info( "消息发送到exchange失败,原因: {}", cause );
            }
        } );
        return rabbitTemplate;
    }


    /**
     * 延迟交换机
     * @return
     */
    @Bean("limitDelayedExchange")
    public TopicExchange limitDelayedExchange(){
        TopicExchange topicExchange = new TopicExchange(MqConstant.VIDEO_BLACKLIST_EXCHANGE, true, false);
        topicExchange.setDelayed(true);
        return topicExchange;
    }

    /**
     * 声明队列信息[自动过期的队列-黑名单受限时间过期队列]
     * @return
     */
    @Bean("limitQueue")
    public Queue limitQueue() {
        return QueueBuilder.durable(MqConstant.VIDEO_BLACKLIST_QUEUE).build();
    }

    /**
     * 绑定延时队列到交换机
     * @param scheduleQueue 状态延时队列
     * @param delayExchange 交换机
     * @return
     */
    @Bean
    public Binding scheduleDelayQueueBinding(@Qualifier("limitQueue") Queue scheduleQueue,
                                             @Qualifier("limitDelayedExchange") TopicExchange delayExchange){
        return BindingBuilder.bind(scheduleQueue).to(delayExchange).with(MqConstant.VIDEO_BLACKLIST_KEY);
    }

}
