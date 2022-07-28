package com.niulijie.mdm.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.niulijie.mdm.constant.SystemConstant;
import com.niulijie.mdm.constant.UserConstant;
import com.niulijie.mdm.entity.ContVideo;
import com.niulijie.mdm.entity.ContVideoComment;
import com.niulijie.mdm.entity.UcUser;
import com.niulijie.mdm.mapper.ContVideoCommentMapper;
import com.niulijie.mdm.mapper.ContVideoMapper;
import com.niulijie.mdm.mapper.UcUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.split;

/**
 * app端消息通知
 *
 * @author df
 * @since 2022-06-08
 */
@Service
public class CommonService {

    @Resource
    private ContVideoMapper contVideoMapper;

    @Resource
    private UcUserMapper ucUserMapper;

    @Resource
    private ContVideoCommentMapper contVideoCommentMapper;

    /**
     * 获取用户信息封装
     */
    public Map<Integer, UcUser> getUserInfo(Set<Integer> userIds) {
        List<UcUser> ucUserList = ucUserMapper.selectList(Wrappers.<UcUser>lambdaQuery()
                .in(UcUser::getUserId,userIds)
                .select(UcUser::getUserId,UcUser::getName, UcUser::getAvatar));
        // 用户主键与用户姓名，头像得映射关系
        return ucUserList.stream().collect(Collectors.toMap(UcUser::getUserId, Function.identity()));
    }

    /**
     * 获取用户黑名单信息
     */
    public boolean getUserAuthInfo(Integer userId) {
        // 或者可以通过sql FIND_IN_SET 查询
        /*ucUserMapper.selectMaps(Wrappers.<UcUser>lambdaQuery()
                .eq(UcUser::getUserId, userId)
                .eq(UcUser::getStatus, UserConstant.NORMAL)
                //不受限状态 apply-拼接sql
                .apply("FIND_IN_SET(2,limit_type)")
                .select(UcUser::getUserId, UcUser::getName));*/

        UcUser ucUser = ucUserMapper.selectOne(Wrappers.<UcUser>lambdaQuery()
                .eq(UcUser::getUserId, userId)
                .select(UcUser::getLimitType));
        if (ObjectUtils.isEmpty(ucUser)) {
            return false;
        }
        String limitType = ucUser.getLimitType();
        List<Integer> split = split(limitType, ",").stream().map(Integer::valueOf).collect(Collectors.toList());
        return split.contains(UserConstant.LIMIT_TYPE_FORBID_COMMENT);
    }

    /**
     * 获取视频id与视频截图地址映射关系
     */
    public Map<Integer, String> getVideoInfo(Set<Integer> videoIds) {
        List<ContVideo> contVideos = contVideoMapper.selectList(Wrappers.<ContVideo>lambdaQuery()
                .in(ContVideo::getVideoId, videoIds)
                .select(ContVideo::getVideoId,ContVideo::getPictureUrl,ContVideo::getDeleted));
        // 视频id与视频截图地址映射关系
        return contVideos.stream().collect(Collectors.toMap(ContVideo::getVideoId,ContVideo::getPictureUrl));
    }

    /**
     * 获取评论信息
     */
    public List<ContVideoComment> getVideoComment(Integer userId) {
        return contVideoCommentMapper.selectList(Wrappers.<ContVideoComment>lambdaQuery()
                .eq(ContVideoComment::getCommentator, userId)
                .eq(ContVideoComment::getDeleted, SystemConstant.NORMAL)
                .select(ContVideoComment::getCommentId));
    }

}
