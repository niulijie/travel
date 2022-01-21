package com.niulijie.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dufa
 * @since 2022-01-11 17:23:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("da_pr_location_dict")
public class DaPrLocationDict implements Serializable {
    private static final long serialVersionUID = -57909878844387068L;
    

    /**
    * 主键
    */
    @TableId(type = IdType.AUTO)
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
