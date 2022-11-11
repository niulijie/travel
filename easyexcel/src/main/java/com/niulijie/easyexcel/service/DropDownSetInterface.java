package com.niulijie.easyexcel.service;

/**
 * 获得数据源的接口
 * @author niuli
 */

public interface DropDownSetInterface {

    /**
     * 获得年级下拉框数据源
     * @param sectionId
     * @return
     */
    String[] getGradeListSource(Integer sectionId);

    String[] getGradeListSource();
}


