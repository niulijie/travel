package com.atguigu.admin.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author dufa
 * @since 2022-01-11 17:23:10
 */
@Data
public class DaPrLocationDict {

    /**
    * 主键
    */
    private Integer id;

    /**
    * 场馆类型表
    */    
    private String locTypeCode;

    /**
    * 场馆类型名称
    */    
    private String locTypeName;

    /**
    * 创建时间
    */    
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
