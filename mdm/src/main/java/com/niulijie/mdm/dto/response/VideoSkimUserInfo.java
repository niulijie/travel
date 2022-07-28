package com.niulijie.mdm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Setter
@Getter
public class VideoSkimUserInfo {

    /**
     * 短视频ID
     */
    private Integer videoId;

    private Integer publisher;

    private String userName;

    private String introduction;

    /**
     * 被评论ID
     */
    private Integer cateId;

    private Float skimRate;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;

}
