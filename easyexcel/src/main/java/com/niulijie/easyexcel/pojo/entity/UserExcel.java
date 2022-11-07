package com.niulijie.easyexcel.pojo.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sharing-backstage
 * @Description: 用户EXCEL类
 * @Author: zwx
 * @Date: 2022/5/12 15:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExcel {

    /**
     * 姓名
     */
    @ExcelProperty(value = {"姓名"}, index = 0)
    private String name;
    /**
     * 测试名称
     */
    @ExcelIgnore
    private String testName;
    /**
     * 工号
     */
    @ExcelProperty(value = {"工号"}, index = 1)
    private String workCode;
    /**
     * 年龄
     */
    @ExcelProperty(value = {"年龄"}, index = 2)
    private String age;
    /**
     * 下拉框
     */
    @ExcelProperty(value = {"下拉框"}, index = 3)
    private String select;

}
