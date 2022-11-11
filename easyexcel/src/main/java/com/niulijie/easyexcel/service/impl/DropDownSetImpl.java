package com.niulijie.easyexcel.service.impl;

import com.niulijie.easyexcel.service.DropDownSetInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * 获得数据源的实现方法
 * @author niuli
 */

@Component
public class DropDownSetImpl implements DropDownSetInterface {

    // 注入service
//    @Autowired
//    private GradeService gradeService;

    //静态初始化当前类
    private static DropDownSetImpl dropDownSet;

    /**
     * 获得年级下拉框数据源
     *
     * @param sectionId
     * @return
     */
    @Override
    public String[] getGradeListSource(Integer sectionId) {
        return new String[0];
    }

    /**
     * 获得年级下拉框数据源
     * @return
     */
    @Override
    public String[] getGradeListSource() {
        //调用service时，通过类来调用
        //List<String> gradeList = dropDownSet.gradeService.getGradeList();
        List<String> gradeList = Arrays.asList("1", "2", "3", "4");
        if (gradeList != null && gradeList .size() > 0) {
            // list 转 String[]
            return gradeList .toArray(new String[gradeList .size()]);
        }
        return null;
    }


    // 在初始化service的方法上加上@PostConstruct注解，使得方法在Bean初始化之后被Spring容器执行
    @PostConstruct
    public void init() {
        dropDownSet = this;
        //dropDownSet.gradeService = this.gradeService;
    }
}

