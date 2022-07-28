package com.niulijie.mdm.dto.param;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 部门用户事件
 */
@Getter
@Setter
public class DeptUserEvent extends ApplicationEvent {

    private List<Integer> deptIds;

    public DeptUserEvent(Object source) {
        super(source);
    }

    public DeptUserEvent(Object source, List<Integer> deptId) {
        super(source);
        this.deptIds = deptId;
    }

}

