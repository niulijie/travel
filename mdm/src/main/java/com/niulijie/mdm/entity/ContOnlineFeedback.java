package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 在线反馈表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("cont_online_feedback")
public class ContOnlineFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 反馈者ID
     */
    private Integer userId;

    /**
     * 回复状态 1-未回复，2-已回复，默认1
     */
    private Integer status;

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
