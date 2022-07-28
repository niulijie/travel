package com.niulijie.mdm.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.niulijie.mdm.constant.SystemConstant;
import com.niulijie.mdm.constant.VideoConstant;
import com.niulijie.mdm.dto.request.ContVideoCommentParam;
import com.niulijie.mdm.dto.response.ContVideoCommentInfo;
import com.niulijie.mdm.dto.response.ContVideoCommentList;
import com.niulijie.mdm.dto.response.ContVideoReplyList;
import com.niulijie.mdm.entity.*;
import com.niulijie.mdm.mapper.*;
import com.niulijie.mdm.result.BusinessException;
import com.niulijie.mdm.result.PageResult;
import com.niulijie.mdm.result.Result;
import com.niulijie.mdm.util.BeanCopierUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * app评论
 *
 * @author df
 * @since 2022-06-08
 */
@Service
@Slf4j
public class AppVideoCommentService {

    @Resource
    private UcUserMapper ucUserMapper;

    @Resource
    private ContVideoMapper contVideoMapper;

    @Resource
    private ContVideoCommentMapper contVideoCommentMapper;

    @Resource
    private ContVideoLikeMapper contVideoLikeMapper;

    @Resource
    private SysSensitiveWordMapper sysSensitiveWordMapper;

    @Resource
    private CommonService commonService;

    @Resource
    private SysDictMapper sysDictMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 添加评论
     */
    public Integer addComment(ContVideoCommentParam contVideoCommentParam) {
        // 权限校验
        commentReplyCheck(contVideoCommentParam);
        ContVideoComment contVideoComment = BeanCopierUtil.copyBean(contVideoCommentParam, ContVideoComment.class);
        contVideoComment.setCommentType(VideoConstant.LIKE_TYPE_VIDEO);
        // 编程性事务，避免大事务
        transactionTemplate.execute(status -> {
            int insert = contVideoCommentMapper.insert(contVideoComment);
            if (insert > 0) {
                contVideoMapper.updateCommentCountById(contVideoCommentParam.getVideoId(),1);
            }
            return Boolean.TRUE;
         });
        return contVideoComment.getCommentId();
    }

    /**
     * 评论回复
     */
    public Integer replyComment(ContVideoCommentParam contVideoCommentParam) {
        // 权限校验
        commentReplyCheck(contVideoCommentParam);
        ContVideoComment contVideoComment = BeanCopierUtil.copyBean(contVideoCommentParam, ContVideoComment.class);
        // 回复人员
        Integer replier = contVideoCommentParam.getReplier();
        // 被评论ID
        Integer cid = contVideoComment.getCid();
        if (!ObjectUtils.isEmpty(replier)) {
            ContVideoComment comment = contVideoCommentMapper.selectOne(Wrappers.<ContVideoComment>lambdaQuery()
                    .eq(ContVideoComment::getCid, cid)
                    .eq(ContVideoComment::getCommentator, replier)
                    .last("limit 1")
                    .select(ContVideoComment::getCommentId, ContVideoComment::getCommentPath)
                    .orderByDesc(ContVideoComment::getCommentId));
            if (!ObjectUtils.isEmpty(comment)) {
                String commentPath = comment.getCommentPath();
                if (StrUtil.isNotBlank(commentPath)) {
                    contVideoComment.setCommentPath(comment.getCommentPath() + "," + comment.getCommentId());
                }
            }
        } else {
            contVideoComment.setCommentPath(cid.toString());
        }
        contVideoComment.setCommentType(VideoConstant.LIKE_TYPE_COMMENT);
        contVideoCommentMapper.insert(contVideoComment);
        return contVideoComment.getCommentId();
    }

    /**
     * 评论回复权限验证封装
     */
    private void commentReplyCheck(ContVideoCommentParam contVideoCommentParam) {
        boolean userAuthInfo = commonService.getUserAuthInfo(contVideoCommentParam.getCommentator());
        if (userAuthInfo) {
            throw new BusinessException(Result.BLACKLIST_LIMIT_USER);
        }
        if (!commentEnableCheck()) {
            throw new BusinessException(Result.COMMENT_ENABLE_FALSE);
        }
        if (!videoCommentEnableCheck(contVideoCommentParam.getVideoId())) {
            throw new BusinessException(Result.VIDEO_COMMENT_ENABLE);
        }
        boolean sensitiveWord = sensitiveWordCheck(contVideoCommentParam.getContent());
        if (sensitiveWord) {
            throw new BusinessException(Result.SENSITIVE_WORD_CHEK);
        }
    }

    /**
     * 敏感词检测，获取所有敏感词
     */
    private boolean sensitiveWordCheck(String text) {
        List<String> sysSensitiveWords = sysSensitiveWordMapper.selectSensitiveWord();
        for (String sysSensitiveWord : sysSensitiveWords) {
            if (text.contains(sysSensitiveWord)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 全局是否可评论
     */
    public boolean commentEnableCheck() {
        SysDict sysDict = sysDictMapper.selectOne(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getKeyCode, VideoConstant.COMMENT_ENABLE_KEY)
                .last("limit 1")
                .select(SysDict::getValue));
        if (ObjectUtils.isEmpty(sysDict)) {
            return true;
        }
        return sysDict.getValue().equals(VideoConstant.COMMENT_ENABLE_VALUE);
    }

    /**
     * 视频是否可评论
     */
    public boolean videoCommentEnableCheck(Integer videoId) {
        ContVideo contVideo = contVideoMapper.selectOne(Wrappers.<ContVideo>lambdaQuery()
                .eq(ContVideo::getVideoId, videoId)
                .last("limit 1")
                .select(ContVideo::getCommented));
        if (ObjectUtils.isEmpty(contVideo)) {
            return true;
        }
        return contVideo.getCommented() == 1;
    }

    /**
     * 评论点赞
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer likeComment(ContVideoCommentParam contVideoCommentParam) {
        Integer commentId = contVideoCommentParam.getCommentId();
        ContVideoLike like = contVideoLikeMapper.selectOne(Wrappers.<ContVideoLike>lambdaQuery()
                .eq(ContVideoLike::getContentId, commentId)
                .eq(ContVideoLike::getPointType,VideoConstant.LIKE_TYPE_COMMENT)
                .last("limit 1")
                .select(ContVideoLike::getId, ContVideoLike::getPoint));
        if (!ObjectUtils.isEmpty(like) && Objects.equals(contVideoCommentParam.getCommentator(), like.getPoint())) {
            throw new BusinessException(Result.COMMENT_LIKE_FINAL);
        }
        ContVideoLike contVideoLike = new ContVideoLike();
        contVideoLike.setContentId(commentId);
        contVideoLike.setPoint(contVideoCommentParam.getCommentator());
        contVideoLike.setPointType(VideoConstant.LIKE_TYPE_COMMENT);
        int insert = contVideoLikeMapper.insert(contVideoLike);
        if (insert > 0) {
            contVideoCommentMapper.likeComment(commentId, 1);
        }
        return insert;
    }

    /**
     * 评论列表
     */
    public Map<String, Object> commentList(ContVideoCommentParam contVideoCommentParam) {
        // 全局是否评论可见
        if (!commentShowCheck()) {
            throw new BusinessException(Result.COMMENT_SHOW_FALSE);
        }
        // 视频是否评论可见
        if (!videoCommentShowCheck(contVideoCommentParam.getVideoId())) {
            throw new BusinessException(Result.VIDEO_COMMENT_SHOW);
        }
        Integer videoId = contVideoCommentParam.getVideoId();
        IPage<ContVideoComment> page = new Page<>(contVideoCommentParam.getCurrent(), contVideoCommentParam.getSize());
        // 所有该视频的评论列表
        IPage<ContVideoComment> contVideoCommentPage = contVideoCommentMapper.selectPage(page, Wrappers.<ContVideoComment>lambdaQuery()
                .eq(ContVideoComment::getVideoId, videoId)
                .eq(ContVideoComment::getCommentType, VideoConstant.LIKE_TYPE_VIDEO)
                .eq(ContVideoComment::getDeleted, SystemConstant.NORMAL)
                .orderByDesc(ContVideoComment::getCommentId)
                .select(ContVideoComment::getCommentId, ContVideoComment::getContent, ContVideoComment::getCommentator,
                        ContVideoComment::getLikeCount, ContVideoComment::getCreateTime));
        List<ContVideoComment> records = contVideoCommentPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageResult.requestSuccessPage(contVideoCommentPage);
        }
        // 获取用户信息所用得查询标识
        Set<Integer> userIds = records.stream().map(ContVideoComment::getCommentator).collect(Collectors.toSet());
        // 查询用户信息
        Map<Integer, UcUser> policeInfos = commonService.getUserInfo(userIds);
        // 获取评论id
        Set<Integer> commentIds = records.stream().map(ContVideoComment::getCommentId).collect(Collectors.toSet());
        // 所有子评论
        List<ContVideoComment> contVideoComments = contVideoCommentMapper.selectList(Wrappers.<ContVideoComment>lambdaQuery()
                .in(ContVideoComment::getCid, commentIds)
                .eq(ContVideoComment::getDeleted, SystemConstant.NORMAL)
                .select(ContVideoComment::getCid, ContVideoComment::getCommentId));
        // Map<被评论的评论ID, List<评论ID集合>>
        Map<Integer, List<Integer>> replyMap = contVideoComments.stream().collect(Collectors.groupingBy(ContVideoComment::getCid, Collectors.mapping(ContVideoComment::getCommentId, Collectors.toList())));
        // 评论的喜欢信息(点赞)
        List<ContVideoLike> contVideoLikes = contVideoLikeMapper.selectLikeInfo(commentIds, contVideoCommentParam.getUserId());
        // Map<被点赞的评论ID, 点赞人>
        Map<Integer, Integer> likeMap = contVideoLikes.stream().collect(Collectors.toMap(ContVideoLike::getContentId, ContVideoLike::getPoint));
        List<ContVideoCommentList> contVideoCommentLists = new ArrayList<>(10);
        for (ContVideoComment contVideoComment : records) {
            ContVideoCommentList videoCommentList = new ContVideoCommentList();
            videoCommentList.setCommentId(contVideoComment.getCommentId());
            videoCommentList.setUserId(contVideoComment.getCommentator());
            videoCommentList.setLikeCount(contVideoComment.getLikeCount());
            videoCommentList.setContent(contVideoComment.getContent());
            // 该评论下有多少条评论
            if (!CollectionUtils.isEmpty(replyMap)) {
                List<Integer> count = replyMap.get(contVideoComment.getCommentId());
                videoCommentList.setReplyCount(CollectionUtils.isEmpty(count) ? 0 : count.size());
            }
            // 设置喜欢状态
            videoCommentList.setLikeStatus(likeMap.get(contVideoComment.getCommentId()) != null);
            UcUser ucUser = policeInfos.get(contVideoComment.getCommentator());
            if (ucUser != null) {
                videoCommentList.setName(ucUser.getName());
                videoCommentList.setAvatar(ucUser.getAvatar());
            }
            videoCommentList.setCreateTime(contVideoComment.getCreateTime());
            contVideoCommentLists.add(videoCommentList);
        }
        return PageResult.requestSuccessPage(contVideoCommentPage, contVideoCommentLists);
    }

    /**
     * 全局评论显示
     */
    public boolean commentShowCheck() {
        SysDict sysDict = sysDictMapper.selectOne(Wrappers.<SysDict>lambdaQuery()
                .eq(SysDict::getKeyCode, VideoConstant.COMMENT_SHOW_KEY)
                .last("limit 1")
                .select(SysDict::getValue));
        if (ObjectUtils.isEmpty(sysDict)) {
            return true;
        }
        return sysDict.getValue().equals(VideoConstant.COMMENT_SHOW_VALUE);
    }

    /**
     * 视频是评论显示
     */
    public boolean videoCommentShowCheck(Integer videoId) {
        ContVideo contVideo = contVideoMapper.selectOne(Wrappers.<ContVideo>lambdaQuery()
                .eq(ContVideo::getVideoId, videoId)
                .last("limit 1")
                .select(ContVideo::getVisualed));
        if (ObjectUtils.isEmpty(contVideo)) {
            return true;
        }
        return contVideo.getVisualed() == 1;
    }

    /**
     * 评论回复列表
     */
    public Map<String, Object> commentReplyList(ContVideoCommentParam contVideoCommentParam) {
        IPage<ContVideoComment> page = new Page<>(contVideoCommentParam.getCurrent(), contVideoCommentParam.getSize());
        // 评论被回复的列表
        IPage<ContVideoComment> contVideoCommentPage = contVideoCommentMapper.selectPage(page, Wrappers.<ContVideoComment>lambdaQuery()
                .eq(ContVideoComment::getCid, contVideoCommentParam.getCommentId())
                .eq(ContVideoComment::getDeleted, SystemConstant.NORMAL)
                .eq(ContVideoComment::getCommentType, VideoConstant.LIKE_TYPE_COMMENT)
                .orderByDesc(ContVideoComment::getCommentId)
                .select(ContVideoComment::getCommentId, ContVideoComment::getContent, ContVideoComment::getCommentator,
                        ContVideoComment::getReplier, ContVideoComment::getLikeCount, ContVideoComment::getCreateTime));
        List<ContVideoComment> records = contVideoCommentPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageResult.requestSuccessPage(contVideoCommentPage);
        }
        // 获取用户信息所用得查询标识
        Set<Integer> userIds = records.stream().map(ContVideoComment::getCommentator).collect(Collectors.toSet());
        Set<Integer> replyUserIds = records.stream().map(ContVideoComment::getReplier).collect(Collectors.toSet());
        userIds.addAll(replyUserIds);
        // 用户主键与用户姓名，头像得映射关系
        Map<Integer, UcUser> policeInfos = commonService.getUserInfo(userIds);
        Set<Integer> commentIds = records.stream().map(ContVideoComment::getCommentId).collect(Collectors.toSet());
        List<ContVideoLike> contVideoLikes = contVideoLikeMapper.selectLikeInfo(commentIds, contVideoCommentParam.getUserId());
        Map<Integer, Integer> likeMap = contVideoLikes.stream().collect(Collectors.toMap(ContVideoLike::getContentId, ContVideoLike::getPoint));
        List<ContVideoReplyList> contVideoReplyLists = new ArrayList<>(10);
        for (ContVideoComment contVideoComment : records) {
            ContVideoReplyList contVideoReplyList = new ContVideoReplyList();
            contVideoReplyList.setCommentId(contVideoComment.getCommentId());
            contVideoReplyList.setLikeCount(contVideoComment.getLikeCount());
            contVideoReplyList.setUserId(contVideoComment.getCommentator());
            contVideoReplyList.setContent(contVideoComment.getContent());
            contVideoReplyList.setLikeStatus(likeMap.get(contVideoComment.getCommentId()) != null);
            UcUser ucUser = policeInfos.get(contVideoComment.getCommentator());
            if (ucUser != null) {
                contVideoReplyList.setName(ucUser.getName());
                contVideoReplyList.setAvatar(ucUser.getAvatar());
            }
            Integer replier = contVideoComment.getReplier();
            if (replier != 0) {
                contVideoReplyList.setReplyId(replier);
                UcUser replyUcUser = policeInfos.get(replier);
                contVideoReplyList.setReplyName(replyUcUser != null ? replyUcUser.getName() : null);
            }
            contVideoReplyList.setCreateTime(contVideoComment.getCreateTime());
            contVideoReplyLists.add(contVideoReplyList);
        }
        return PageResult.requestSuccessPage(contVideoCommentPage, contVideoReplyLists);
    }

    /**
     * 评论删除
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteComment(ContVideoCommentParam contVideoCommentParam) {
        Integer commentId = contVideoCommentParam.getCommentId();
        ContVideoComment contVideoComment = contVideoCommentMapper.selectOne(Wrappers.<ContVideoComment>lambdaQuery()
                .eq(ContVideoComment::getCommentId, commentId)
                .select(ContVideoComment::getVideoId, ContVideoComment::getCid, ContVideoComment::getCommentator));
        // 只有自己添加的评论才有权删除
        if (ObjectUtils.isEmpty(contVideoComment) || !contVideoComment.getCommentator().equals(contVideoCommentParam.getCommentator())) {
            throw new BusinessException(Result.COMMENT_DELETE_AUTH);
        }
        // cid = 0，没有下级评论，cid ！= 0，有下级评论
        int cid = contVideoComment.getCid() == 0 ? commentId : contVideoComment.getCid();
        // 同时删除了本评论 和 下级评论
        int update = contVideoCommentMapper.update(null, Wrappers.<ContVideoComment>lambdaUpdate()
                .set(ContVideoComment::getDeleted, SystemConstant.DELETE)
                .eq(ContVideoComment::getCommentId, commentId)
                .or()
                .eq(ContVideoComment::getCid, cid)
                .like(ContVideoComment::getCommentPath, commentId));
        if (update > 0) {
            contVideoMapper.updateCommentCountById(contVideoComment.getVideoId(), -update);
        }
        return update;
    }

    /**
     * 评论点赞取消
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer likeCommentCancel(ContVideoCommentParam contVideoCommentParam) {
        Integer commentId = contVideoCommentParam.getCommentId();
        ContVideoLike like = contVideoLikeMapper.selectOne(Wrappers.<ContVideoLike>lambdaQuery()
                .eq(ContVideoLike::getContentId, contVideoCommentParam.getCommentId())
                .eq(ContVideoLike::getPointType,VideoConstant.LIKE_TYPE_COMMENT)
                .last("limit 1")
                .select(ContVideoLike::getId, ContVideoLike::getPoint));
        if (ObjectUtils.isEmpty(like)) {
            throw new BusinessException(Result.COMMENT_LIKE_NULL);
        }
        if (!Objects.equals(contVideoCommentParam.getCommentator(), like.getPoint())) {
            throw new BusinessException(Result.COMMENT_LIKE_AUTH);
        }
        int insert = contVideoLikeMapper.delete(Wrappers.<ContVideoLike>lambdaUpdate()
                .eq(ContVideoLike::getContentId, commentId));
        if (insert > 0) {
            contVideoCommentMapper.likeComment(commentId, -1);
        }
        return insert;
    }

    /**
     * 根据评论id查询评论信息
     */
    public ContVideoCommentInfo commentInfo(ContVideoCommentParam contVideoCommentParam) {
        ContVideoComment contVideoComment = contVideoCommentMapper.selectOne(Wrappers.<ContVideoComment>lambdaQuery()
                .eq(ContVideoComment::getCommentId, contVideoCommentParam.getCommentId())
                .select(ContVideoComment::getCommentId, ContVideoComment::getContent, ContVideoComment::getCommentator,
                        ContVideoComment::getLikeCount, ContVideoComment::getCid, ContVideoComment::getCreateTime));
        if (ObjectUtils.isEmpty(contVideoComment)) {
            return null;
        }
        // 首层评论 - 视频的评论
        if (contVideoComment.getCid() == 0) {
            // 获取评论者信息以及该评论下的评论数
            return getUserAndReplyInfo(contVideoCommentParam.getCommentId(), contVideoComment);
        }
        // 对评论的评论 - (先找的主评论信息)
        ContVideoComment videoComment = contVideoCommentMapper.selectOne(Wrappers.<ContVideoComment>lambdaQuery()
                .eq(ContVideoComment::getCommentId, contVideoComment.getCid()) //被评论ID
                .last("limit 1")
                .select(ContVideoComment::getCommentId, ContVideoComment::getContent, ContVideoComment::getCommentator,
                        ContVideoComment::getLikeCount, ContVideoComment::getCid, ContVideoComment::getCreateTime));
        return getUserAndReplyInfo(contVideoComment.getCid(), videoComment);
    }

    /**
     * 获取其他信息方法封装
     * @param commentId 评论ID
     * @param contVideoComment 评论内容
     * @return
     */
    private ContVideoCommentInfo getUserAndReplyInfo(Integer commentId, ContVideoComment contVideoComment) {
        UcUser ucUser = ucUserMapper.selectOne(Wrappers.<UcUser>lambdaQuery()
                .eq(UcUser::getUserId, contVideoComment.getCommentator()) //评论者ID
                .select(UcUser::getName, UcUser::getAvatar));
        ContVideoCommentInfo videoCommentReply = BeanCopierUtil.copyBean(contVideoComment, ContVideoCommentInfo.class);
        videoCommentReply.setUserId(contVideoComment.getCommentator());
        if (!ObjectUtils.isEmpty(ucUser)) {
            videoCommentReply.setName(ucUser.getName());
            videoCommentReply.setAvatar(ucUser.getAvatar());
        }
        List<ContVideoComment> contVideoComments = contVideoCommentMapper.selectList(Wrappers.<ContVideoComment>lambdaQuery()
                .in(ContVideoComment::getCid, commentId)
                .eq(ContVideoComment::getDeleted, SystemConstant.NORMAL)
                .select(ContVideoComment::getCommentId));
        if (!CollectionUtils.isEmpty(contVideoComments)) {
            videoCommentReply.setReplyCount(contVideoComments.size());
        }
        return videoCommentReply;
    }

    /**
     * 消息通知总数量
     */
    public Long allCount() {
        LongAdder longAdder = new LongAdder();
        longAdder.add(1);
        longAdder.add(2);
        longAdder.add(3);
        longAdder.add(4);
        return longAdder.longValue();
    }
}
