package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
    * 服务内部错误日志表
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "service_error_log")
public class ServiceErrorLog {
    @TableId(value = "error_id", type = IdType.INPUT)
    private Integer errorId;

    /**
     * 请求参数
     */
    @TableField(value = "error_result")
    private String errorResult;

    /**
     * 请求路径
     */
    @TableField(value = "error_url")
    private String errorUrl;

    /**
     * 错误信息
     */
    @TableField(value = "error_info")
    private String errorInfo;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}