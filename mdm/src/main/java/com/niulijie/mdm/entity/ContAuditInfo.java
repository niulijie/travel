package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 审核记录表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("cont_audit_info")
public class ContAuditInfo implements Serializable {

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
     * 视频发布者id
     */
    private Integer publisher;

    /**
     * 审核人ID
     */
    private Integer userId;

    /**
     * 审核人所属部门
     */
    private Integer deptId;

    /**
     * 审核状态 1-审核通过、2-审核驳回
     */
    private Integer auditStatus;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

}
