package com.niulijie.mdm.dto.message;

import lombok.Builder;
import lombok.Data;

/**
* @description   置顶视频消息体
* @author niulj
* @date 2022/6/20 17:48
*/
@Data
@Builder
public class SendLimitUserMessage {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 受限结束时间
     */
    private Long limitTime;

}
