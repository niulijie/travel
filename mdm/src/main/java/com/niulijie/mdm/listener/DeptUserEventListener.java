package com.niulijie.mdm.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.niulijie.mdm.dto.cache.DeptUserCache;
import com.niulijie.mdm.dto.param.DeptUserEvent;
import com.niulijie.mdm.entity.UcUserDept;
import com.niulijie.mdm.mapper.UcUserDeptMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 事件监听，获取更新的部门信息，重建缓存
 * @author df
 */
@Slf4j
@Component
public class DeptUserEventListener implements ApplicationListener<DeptUserEvent> {

    @Resource
    private UcUserDeptMapper ucUserDeptMapper;

    @Value("${top.deptId}")
    private Integer topDeptId;

    @Override
    public void onApplicationEvent(DeptUserEvent event) {
        log.info("mq获取到更新得部门信息：{}", event.getDeptIds());
        Map<Integer, Integer> deptUserCache = DeptUserCache.deptUserCache;
        Long count = ucUserDeptMapper.selectCount(Wrappers.<UcUserDept>lambdaQuery()
                .eq(UcUserDept::getStatus, 0).select(UcUserDept::getUserId));
        deptUserCache.put(topDeptId, count.intValue());
    }

}

