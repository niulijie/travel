package com.niulijie.mdm.dto.response;

/**
 * 批量给视频收藏赋值接口
 * @author niuli
 */
public interface IBatchSetCollectStatus {

    /**
     * 获取视频id
     * @return
     */
    Integer batchGetVideoId();

    /**
     * 设置用户收藏状态
     * @param collectFlag
     */
    void batchSetCollectStatus(Boolean collectFlag);

}
