package com.niulijie.mdm.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dufa
 *  通用常量
 * @date 2020/11/4 13:11
 * @version v1.0
 */
public interface ExcelFileConstant {

    /**
     * Excel文件头常量信息
     */
    List<String> EXCEL_HEAD = Collections.singletonList("敏感词");

    /**
     * 文件类型
     */
    List<String> FILE_TYPE = Arrays.asList("xlsx","xls");

    /**
     * 座机格式正则
     */
    String TEL_REGEX = "\\d{3,4}[-]\\d{7,8}";

    /**
     * 手机号格式正则
     */
    String PHONE_REGEX = "^1[3-9]\\d{9}$";

}

