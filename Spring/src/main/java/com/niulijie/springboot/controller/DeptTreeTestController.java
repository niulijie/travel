package com.niulijie.springboot.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ArrayListMultimap;
import com.niulijie.springboot.entity.UcDept;
import com.niulijie.springboot.mapper.UcDeptMapper;
import com.niulijie.springboot.util.BeanCopierUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dept/tree")
public class DeptTreeTestController {

    @Autowired
    private UcDeptMapper deptMapper;

    @GetMapping("/test")
    @ResponseBody
    public void test(){
        List<UcDept> depts = deptMapper.selectList(Wrappers.<UcDept>lambdaQuery());
        recursionTransition(depts, 0);
    }
    /**
     * 递归获取树层结构
     */
    public List<UcDept> recursionTransition(List<UcDept> primaryObject, Integer topNode) {
        // 自定义的 bean 复制对象 ( 可以自己写一个  就是把原来的对象 复制到新的对象里面,
        //  其实就是多个一个 list<latestObject> 的参数 可以进行递归节点操作 )
        List<UcDept> latestObjectList = BeanCopierUtil.copyBeanList(primaryObject, UcDept.class);
        // 创建一个 一个key 多个value 的这个对象
        ArrayListMultimap<Integer, UcDept> multiMap = ArrayListMultimap.create();
        List<UcDept> latestObjectListA = new ArrayList<>();
        latestObjectList.forEach(latestObject-> {
            multiMap.put(latestObject.getParentId(), latestObject);
            if (latestObject.getParentId() == (topNode)) {
                //顶级部门列表
                latestObjectListA .add(latestObject);
            }
        });

        regionalLevel(multiMap, latestObjectListA, topNode);

        return latestObjectListA;
    }

    /**
     * 递归遍历对象
     * @param multiMap 所有对象集合
     * @param latestObjectListA 本级最高节点
     * @param id
     */
    public void regionalLevel(ArrayListMultimap<Integer, UcDept> multiMap, List<UcDept> latestObjectListA , Integer id) {
        latestObjectListA .forEach(latestObject-> {
            List<UcDept> ucDepts = multiMap.get(latestObject.getDeptId());
            if (!CollectionUtils.isEmpty(ucDepts)) {
                latestObject.setChildrenList(ucDepts);
                regionalLevel(multiMap, ucDepts, latestObject.getDeptId());
            }
        });
    }
}
