package com.niulijie.mdm.publisher;

import cn.hutool.json.JSONUtil;
import com.niulijie.mdm.constant.MqConstant;
import com.niulijie.mdm.dto.message.SendLimitUserMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author niulijie
 * @description 发送MQ消息
 * @date 2021/12/6 13:41
 */
@Slf4j
@Component
public class MessageProducer {

    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * @Param  sendScheduleCallGroupMessage,daleyTime
     * @description  添加黑名单受限结束时间消息
     * @author niulj
     * @date 2021/12/6 14:19
     */
    @Async("threadPoolExecutor")
    public void sendLimitUserMessage(SendLimitUserMessage sendLimitUserMessage, Long daleyTime) {
        log.info("<<<黑名单受限结束时间：{}>>>", sendLimitUserMessage.getLimitTime());
        amqpTemplate.convertAndSend(MqConstant.VIDEO_BLACKLIST_EXCHANGE,
                MqConstant.VIDEO_BLACKLIST_KEY,
                sendLimitUserMessage,
                message -> {
                    message.getMessageProperties().setHeader("x-delay", daleyTime);
                    log.info("<<<添加黑名单受限结束时间消息信息:{},消息到期时间：{}>>>", JSONUtil.toJsonStr(sendLimitUserMessage), daleyTime);
                    return message;
                });
    }
}
