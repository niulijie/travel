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
 * @since 2021-12-25 17:08:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("da_pr_post")
public class DaPrPost implements Serializable {
    private static final long serialVersionUID = -11219578815898866L;


    /**
    * 岗位id
    */
    @TableId(type = IdType.AUTO)
    private Integer postId;

    /**
     * 岗位码
     */
    private String postCode;

    /**
    * 岗位名称
    */    
    private String postName;

    /**
     * 岗位全路径
     */
    private String postFullName;

    /**
     * 场馆编号
     */
    private String locNum;

    /**
    * 0 删除 1正常
    */    
    private Integer status;

    /**
     * 场馆内外标识 1 场馆内 2 场馆外
     */
    private Integer postStatus;

    /**
     * 扫码标识 0 不扫 1 扫码 默认0
     */
    private Integer scanStatus;

    /**
     * 1 2 3级
     */
    private Integer postLevel;

    /**
     * 闭环内外标识 0 不区分 1闭环内 2 闭环外
     */
    private Integer postCircle;


    /**
     * 二维码base64编码
     */
    private String codeImg;

    /**
    * 创建时间
    */
    private LocalDateTime createTime;

    /**
    * 更新时间
    */    
    private LocalDateTime updateTime;

}
