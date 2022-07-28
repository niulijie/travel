package com.niulijie.mdm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.niulijie.mdm.dto.response.VideoCommentUserInfo;
import com.niulijie.mdm.entity.ContVideoComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Mapper
public interface ContVideoCommentMapper extends BaseMapper<ContVideoComment> {

    Integer likeComment(@Param("commentId") Integer commentId, @Param("value") Integer value);

    IPage<VideoCommentUserInfo> selectPageInfo(@Param("page") IPage<VideoCommentUserInfo> page,
                                               @Param("videoIds") List<Integer> videoIds, @Param("searchKey") String searchKey);
}
