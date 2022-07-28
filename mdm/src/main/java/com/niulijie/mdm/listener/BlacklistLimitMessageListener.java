package com.niulijie.mdm.listener;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.niulijie.mdm.constant.MqConstant;
import com.niulijie.mdm.constant.UserConstant;
import com.niulijie.mdm.entity.UcUser;
import com.niulijie.mdm.mapper.UcUserMapper;
import com.niulijie.mdm.dto.message.SendLimitUserMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @description 黑名单受限结束时间消息监听类
 * * @author niulj
 * * @date 2021/12/29 18:20
 */
@Slf4j
@Component
public class BlacklistLimitMessageListener {

    @Autowired
    private UcUserMapper ucUserMapper;

    /*@Resource
    private RedisTemplate<String, Object> redisTemplate;*/

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConstant.VIDEO_BLACKLIST_QUEUE),
            exchange = @Exchange(value = MqConstant.VIDEO_BLACKLIST_EXCHANGE,
                    type = "x-delayed-message",
                    ignoreDeclarationExceptions = "true"),
            key = MqConstant.VIDEO_BLACKLIST_KEY)
    )
    @RabbitHandler
    public void consumerMessage(Message message, Channel channel) throws IOException {
        log.info("<<<黑名单受限结束时间消息监听类监听到的消息体：{}",new String(message.getBody()));
        try {
            //获取消息体
            SendLimitUserMessage sendLimitUserMessage = JSONUtil.toBean(new String(message.getBody()), SendLimitUserMessage.class);
            UcUser ucUser = ucUserMapper.selectOne(Wrappers.<UcUser>lambdaQuery()
                    .eq(UcUser::getUserId, sendLimitUserMessage.getUserId())
                    .eq(UcUser::getStatus, UserConstant.NORMAL)
                    .last("limit 1")
                    .select(UcUser::getUserId, UcUser::getLimitType, UcUser::getLimitTime));
            if (!ObjectUtils.isEmpty(ucUser) && !String.valueOf(UserConstant.LIMIT_TYPE_NO).contains(ucUser.getLimitType())) {
                //结束时间一致，取消受限
                LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(sendLimitUserMessage.getLimitTime(), 0, ZoneOffset.of("+8"));
                LocalDate limitTime = localDateTime.toLocalDate();
                if(ucUser.getLimitTime().equals(limitTime)){
                    UcUser ucUserTemp = new UcUser();
                    ucUserTemp.setLimitType(String.valueOf(UserConstant.LIMIT_TYPE_NO));
                    ucUserMapper.update(ucUserTemp, Wrappers.<UcUser>lambdaQuery().eq(UcUser::getUserId, ucUser.getUserId()));
                }
            }

            //确认消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            log.info("黑名单受限结束消费信息信息错误", e);
        } finally {
            //redisTemplate.expire(RedisKeyConstant.COMMENT_AUDIT,0, TimeUnit.SECONDS);
        }
    }
}
