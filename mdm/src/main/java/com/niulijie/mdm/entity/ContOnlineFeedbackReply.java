package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author dufa
 * @since 2022-06-15 11:09:21
 */
@Data
@TableName("cont_online_feedback_reply")
public class ContOnlineFeedbackReply implements Serializable {
    private static final long serialVersionUID = -85957088049758827L;
    

    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 在线反馈ID
    */    
    private Integer feedbackId;

    /**
    * 回复内容
    */    
    private String replyContent;

    /**
    * 回复人员
    */    
    private Integer creator;

    /**
    * 回复时间
    */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
