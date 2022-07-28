package com.niulijie.mdm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niulijie.mdm.dto.param.Dept;
import com.niulijie.mdm.entity.UcDept;
import com.niulijie.mdm.mapper.UcDeptMapper;
import com.niulijie.mdm.util.BeanCopierUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class UcDeptService extends ServiceImpl<UcDeptMapper, UcDept> {

    /**
     * 获取当前部门以及下级部门id (单个)
     */
    public List<Integer> getDeptLevelId(Integer deptId) {
        return this.baseMapper.getDeptLevelId(deptId);
    }

    /**
     * 获取当前部门以及下级部门id (多个)
     */
    public Set<Integer> getDeptLevelId(List<Integer> deptIdList) {
        Set<Integer> deptLevelIdList = new TreeSet<>();
        deptIdList.forEach(deptId -> {
            List<Integer> deptLevelId = this.baseMapper.getDeptLevelId(deptId);
            deptLevelIdList.addAll(deptLevelId);
        });
        return deptLevelIdList;
    }

    /**
     * 部门新增
     * @param dept
     */
    public void addDept(Dept dept) {
        UcDept ucDept = BeanCopierUtil.copyBean(dept, UcDept.class);
        this.saveOrUpdate(ucDept);
    }

    /**
     * 部门更新
     * @param dept
     */
    public void updateDept(Dept dept) {
        UcDept ucDept = BeanCopierUtil.copyBean(dept, UcDept.class);
        this.saveOrUpdate(ucDept);
    }

    /**
     * 部门删除 - 修改状态为删除状态
     * @param dept
     */
    public void deleteDept(Dept dept) {
        UcDept ucDept = BeanCopierUtil.copyBean(dept, UcDept.class);
        this.updateById(ucDept);
    }
}
