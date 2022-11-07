package com.niulijie.easyexcel.pojo.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 企业属性实体
 * @author sunyi
 */
@Data
public class HeadAttrSimple implements Serializable {

    private Integer isNull = 0;

    /**
     * 属性ID
     */
    private String attrId;

    private String attrName;

    /**
     * 属性字段名
     */
    private String attrField;

    private Integer attrType;

    private String rule;

    private List<String> items;

}
