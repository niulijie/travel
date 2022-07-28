package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niulijie.mdm.dto.request.ContVideoQueryParam;
import com.niulijie.mdm.dto.response.ContVideoAppDetailResp;
import com.niulijie.mdm.dto.response.ContVideoInfo;
import com.niulijie.mdm.dto.response.ContVideoManagerResp;
import com.niulijie.mdm.dto.response.VideoHotUserInfo;
import com.niulijie.mdm.entity.ContVideo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 短视频表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */

@Mapper
public interface ContVideoMapper extends BaseMapper<ContVideo> {

    Integer updateCollectCountById(@Param("videoId") Integer videoId, @Param("value") Integer value);

    Integer updateCommentCountById(@Param("videoId") Integer videoId, @Param("count") Integer count);

    Integer updateCommentCountByIds(@Param("videoIds") Set<Integer> videoIds, @Param("count") Integer count);

    /**
     *  点赞数统计 +1 操作
     */
    void likeCountIncrease(@Param("contentId") Integer contentId);

    /**
     *  点赞数统计 -1 操作
     */
    void likeCountReduce( @Param("contentId") Integer contentId);

    /**
     * 查询视频列表--PC端页面
     */
    IPage<ContVideoManagerResp> selectPageList(@Param("page") Page page, @Param("cateId") Integer cateId,
                                               @Param("searchKey") String searchKey, @Param("deptIdList") List<Integer> deptIdList);

    /**
     * 更新视频浏览数和浏览时间
     */
    Integer updateVideoSkimInfo(@Param("videoId") Integer videoId, @Param("duration") Integer duration);

    List<Integer> selectVideoIds(Integer cateId);

    /**
     * 批量查询视频所有详情
     */
    IPage<ContVideoAppDetailResp> selectPageDetail(@Param("page") IPage<ContVideoAppDetailResp> page, @Param("queryParam") ContVideoQueryParam queryParam);

    /**
     * 推荐视频列表
     * @param page
     * @param queryParam
     * @return
     */
    IPage<ContVideoAppDetailResp> selectRecommendList(@Param("page") IPage<ContVideoAppDetailResp> page, @Param("queryParam") ContVideoQueryParam queryParam);

    /**
     * 批量获取视频信息通过Md5值
     * @param md5List
     * @return
     */
    @MapKey("videoMd5")
    Map<String, Map<String,Object>> selectVideoByMd5(@Param("md5List") List<String> md5List);

    /**
     * 根据视频id查询视频信息
     */
    ContVideoInfo selectVideoInfo(Integer videoId);

    IPage<VideoHotUserInfo> selectPageInfo(@Param("page") IPage<VideoHotUserInfo> page, @Param("videoIds") List<Integer> videoIds,
                                           @Param("searchKey") String searchKey, @Param("date") LocalDateTime date);

}
