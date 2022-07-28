package com.niulijie.mdm.publisher;

import com.niulijie.mdm.dto.param.DeptUserEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息发布者
 * @author df
 */
@Component
public class DeptUserEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 发布消息
     * @param deptIds
     */
    public void sendDeptUserEvent(List<Integer> deptIds) {
        DeptUserEvent deptUserEvent = new DeptUserEvent(this, deptIds);
        applicationEventPublisher.publishEvent(deptUserEvent);
    }

}

