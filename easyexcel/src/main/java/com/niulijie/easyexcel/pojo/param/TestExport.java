package com.niulijie.easyexcel.pojo.param;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class TestExport {
    @ExcelProperty("key")
    private String key;
    @ExcelProperty("value")
    private String value;
}
