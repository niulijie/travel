package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 足迹表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("cont_video_trace")
public class ContVideoTrace implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 短视频ID
     */
    private Integer videoId;

    /**
     * 浏览者ID
     */
    private Integer userId;

    /**
     * 浏览时长 单位：秒
     */
    private Integer duration;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
