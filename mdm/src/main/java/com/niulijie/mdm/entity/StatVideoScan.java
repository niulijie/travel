package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dufa
 * @since 2022-07-14 10:05:35
 */
@Data
@TableName("stat_video_scan")
public class StatVideoScan implements Serializable {
    private static final long serialVersionUID = 615905578199728716L;
    

    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 视频id
    */    
    private Integer videoId;

    /**
    * 浏览用户所在部门
    */    
    private Integer deptId;

    /**
    * 浏览用户数量
    */    
    private Integer skimUserCount;

    /**
    * 浏览率
    */    
    private Float skimRate;

    /**
    * 创建时间
    */    
    private LocalDateTime createTime;

}
