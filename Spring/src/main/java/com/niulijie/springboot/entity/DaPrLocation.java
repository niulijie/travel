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
 * @since 2021-12-25 17:08:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("da_pr_location")
public class DaPrLocation implements Serializable {
    private static final long serialVersionUID = 268426895540168919L;
    

    /**
    * 场馆id
    */
    @TableId(type = IdType.AUTO)
    private Integer locId;

    /**
    * 场馆编号
    */    
    private String locNum;

    /**
    * 场馆名称
    */    
    private String locName;

    /**
     * 场馆类别码
     */
    private String locTypeCode;

    /**
    * 场所纬度
    */    
    private String latitude;

    /**
    * 场所经度
    */    
    private String longitude;

    /**
    * 0 删除 1正常
    */    
    private Integer locStatus;

    /**
    * 创建时间
    */    
    private LocalDateTime createTime;

    /**
    * 更新时间
    */    
    private LocalDateTime updateTime;

}
