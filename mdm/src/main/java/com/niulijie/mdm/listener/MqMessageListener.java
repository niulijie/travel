package com.niulijie.mdm.listener;

import cn.hutool.json.JSONUtil;
import com.niulijie.mdm.dto.enums.ActionTypeEnum;
import com.niulijie.mdm.dto.param.Dept;
import com.niulijie.mdm.dto.param.User;
import com.niulijie.mdm.service.UcDeptService;
import com.niulijie.mdm.service.UcUserService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

/**
 *
 * @author niuli
 * @description 队列消息监听类
 */
@Component
@Slf4j
public class MqMessageListener {

    @Resource
    private UcUserService ucUserService;

    @Resource
    private UcDeptService ucDeptService;

    /**
     * 同步用户数据监听处理
     *
     * @param message 消息
     * @param channel 通信通道
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.binding.queue.user}"),
            exchange = @Exchange(value = "${spring.rabbitmq.binding.exchange.sync.name}",
                    type = "${spring.rabbitmq.binding.exchange.sync.type}",
                    ignoreDeclarationExceptions = "true"),
            key = "${spring.rabbitmq.binding.key.user}"),
            containerFactory = "ucenterListenerFactory"
    )
    public void syncUserData(Message message, Channel channel) throws IOException {
            log.info("接收到用户的变更数据为:{}", new String(message.getBody()));
            User user = JSONUtil.toBean(new String(message.getBody()), User.class);
            //用户信息和身份证号不能为空[为空不作处理]
            if (!ObjectUtils.isEmpty(user) && !StringUtils.isEmpty(user.getIdCard())) {
                ActionTypeEnum enumByType = ActionTypeEnum.getEnumByType(user.getActionType(), ActionTypeEnum.class);
                switch (enumByType) {
                    //用户新增
                    case ADD:
                        log.info("用户新增:{}", JSONUtil.toJsonStr(user));
                        ucUserService.addUser(user);
                        log.info("用户[{}]新增完成", user.getIdCard());
                        break;
                    //用户更新
                    case UPDATE:
                        log.info("用户更新:{}", JSONUtil.toJsonStr(user));
                        ucUserService.updateUser(user);
                        log.info("用户[{}]更新完成", user.getIdCard());
                        break;
                    //用户删除
                    case DELETE:
                        log.info("用户删除:{}", JSONUtil.toJsonStr(user));
                        ucUserService.deleteUser(user);
                        log.info("用户[{}]删除完成", user.getIdCard());
                        break;
                    //其他不作处理
                    default:
                        break;
                }
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 同步部门数据监听处理
     * @param message 消息
     * @param channel 通信通道
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.binding.queue.dept}"),
            exchange = @Exchange(value = "${spring.rabbitmq.binding.exchange.sync.name}",
                    type = "${spring.rabbitmq.binding.exchange.sync.type}",
                    ignoreDeclarationExceptions = "true"),
            key = "${spring.rabbitmq.binding.key.dept}")
    )
    public void syncDeptData(Message message, Channel channel) throws IOException {
            log.info("接收到部门的变更数据为:{}", new String(message.getBody()));
            Dept dept = JSONUtil.toBean(new String(message.getBody()), Dept.class);
            //部门信息和部门ID不能为空[为空不作处理]
            if (!ObjectUtils.isEmpty(dept) && !StringUtils.isEmpty(dept.getDeptId())) {
                ActionTypeEnum enumByType = ActionTypeEnum.getEnumByType(dept.getActionType(), ActionTypeEnum.class);
                switch (enumByType) {
                    //部门新增
                    case ADD:
                        log.info("部门新增:{}", JSONUtil.toJsonStr(dept));
                        ucDeptService.addDept(dept);
                        log.info("部门[{}]新增完成", dept.getDeptId());
                        break;
                    //部门更新
                    case UPDATE:
                        log.info("部门更新:{}", JSONUtil.toJsonStr(dept));
                        ucDeptService.updateDept(dept);
                        log.info("部门[{}]更新完成", dept.getDeptId());
                        break;
                    //部门删除
                    case DELETE:
                        log.info("部门删除:{}", JSONUtil.toJsonStr(dept));
                        ucDeptService.deleteDept(dept);
                        log.info("部门[{}]删除完成", dept.getDeptId());
                        break;
                    //其他不作处理
                    default:
                        break;
                }
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
