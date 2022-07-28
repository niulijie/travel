package com.niulijie.mdm.dto.response;

import java.io.Serializable;

/**
 * 批量给视频喜欢赋值接口
 * @author niuli
 */
public interface IBatchSetLikeStatus extends Serializable {
    /**
     * 获取视频id
     * @return
     */
    Integer batchGetVideoId();

    /**
     * 设置用户喜欢状态的方法
     * @param likeFlag
     */
    void batchSetLikeStatus(Boolean likeFlag);

}
