package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 喜欢信息表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("cont_video_like")
public class ContVideoLike implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 短视频或评论ID
     */
    private Integer contentId;

    /**
     * 发布者
     */
    private Integer publisher;

    /**
     * 喜欢者
     */
    private Integer point;

    /**
     * 点赞对象：0-短视频、1-评论
     */
    private Integer pointType;

    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
