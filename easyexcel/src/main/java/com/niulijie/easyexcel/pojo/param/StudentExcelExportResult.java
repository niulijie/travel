package com.niulijie.easyexcel.pojo.param;


import com.alibaba.excel.annotation.ExcelProperty;
import com.niulijie.easyexcel.annotation.DropDownSetField;
import com.niulijie.easyexcel.service.impl.DropDownSetImpl;
import lombok.Data;

/**
 * @author niuli
 */
@Data
public class StudentExcelExportResult{

    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "学号")
    private String code;

    @ExcelProperty(value = "性别")
    @DropDownSetField(source = {"男","女"})		// 固定数据源
    private String gender;

    @ExcelProperty(value = "年级")
    @DropDownSetField(sourceClass = DropDownSetImpl.class)		// 动态数据源
    private String gradeList;

    @ExcelProperty(value = "科目")
    private String subject;
}

