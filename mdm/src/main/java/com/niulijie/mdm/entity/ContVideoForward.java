package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

@TableName(value = "cont_video_forward")
public class ContVideoForward {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 短视频id
     */
    private Integer videoId;

    /**
     * 转发人员id
     */
    private Integer userId;

    /**
     * 转发人员部门
     */
    private Integer deptId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}