package com.niulijie.mdm.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niulijie.mdm.constant.SystemConstant;
import com.niulijie.mdm.constant.VideoConstant;
import com.niulijie.mdm.dto.param.ContVideoParam;
import com.niulijie.mdm.dto.request.ContVideoQueryParam;
import com.niulijie.mdm.dto.request.FileBatchUploadRequest;
import com.niulijie.mdm.dto.response.ContVideoAppListResp;
import com.niulijie.mdm.dto.response.UploadFileResp;
import com.niulijie.mdm.entity.ContVideo;
import com.niulijie.mdm.entity.ContVideoComment;
import com.niulijie.mdm.mapper.ContVideoCommentMapper;
import com.niulijie.mdm.mapper.ContVideoMapper;
import com.niulijie.mdm.publisher.MessageProducer;
import com.niulijie.mdm.result.BusinessException;
import com.niulijie.mdm.result.PageResult;
import com.niulijie.mdm.result.Result;
import com.niulijie.mdm.util.BeanCopierUtil;
import com.niulijie.mdm.util.DateUtil;
import com.niulijie.mdm.util.ImageUtils;
import com.niulijie.mdm.util.MinioUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class ContVideoService extends ServiceImpl<ContVideoMapper, ContVideo> {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private ContVideoMapper contVideoMapper;

    @Autowired
    private ContVideoCommentMapper contVideoCommentMapper;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private UcUserService ucUserService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 上传视频文件接口
     *
     * @param request 请求Request
     * @return
     */
    public List<UploadFileResp> upload(FileBatchUploadRequest request) throws ExecutionException, InterruptedException {
        List<UploadFileResp> uploadFileRespList = new ArrayList<>();
        List<MultipartFile> multipartFileList = request.getFiles();
        if (!CollectionUtils.isEmpty(multipartFileList)) {
            // 生成上传视频任务
            CompletableFuture[] completableFutures = multipartFileList.stream()
                    .map(multipartFile -> CompletableFuture.supplyAsync(() -> uploadFile(multipartFile), threadPoolExecutor))
                    .toArray(CompletableFuture[]::new);
            // 等待所有任务执行完成触发
            CompletableFuture.allOf(completableFutures);
            for (Future<UploadFileResp> future : completableFutures) {
                //获取任务返回结果并且封装
                UploadFileResp uploadFileResp = future.get();
                uploadFileRespList.add(uploadFileResp);
            }
        }
        multipartFileList.clear();
        return uploadFileRespList;
    }

    /**
     * 单文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public UploadFileResp uploadFile(MultipartFile file) {
        //视频的云端地址
        String videoUrl = "";
        //图片的云端地址
        String pictureUrl = "";
        UploadFileResp uploadFileResp = new UploadFileResp();
        // 1. 校验文件格式
        String title = file.getOriginalFilename();
        checkVideoType(title);

        // 2. 获取文件md5
        String md5 = "";
        try {
            md5 = DigestUtils.md5DigestAsHex(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(md5)) {
            // 3. 查询视频表中视频md5是否存在, 有直接赋值，否则上传视频
            ContVideo video = getContVideo(md5);
            if (ObjectUtils.isEmpty(video)) {
                // 4.拼接新存储名字
                String fileName = minioUtil.date() + RandomUtil.randomString(4) + title.substring(title.lastIndexOf("."));

                // 5.上传视频，测试环境走自己minio，否则将视频上传青牛
                videoUrl = minioUtil.uploadVideo(file, fileName);

                //6. 将远端视频的某帧作为缩略图并上传远端 上传视频，如果成功则返回视频远端地址，如果上传失败，视频远端地址为空
                if (!StringUtils.isEmpty(videoUrl)) {
                    pictureUrl = minioUtil.updatePicByUrl(videoUrl, fileName);
                }
                uploadFileResp.setFileName(fileName);
                uploadFileResp.setVideoMd5(md5);
                uploadFileResp.setTitle(title);
                uploadFileResp.setPictureUrl(pictureUrl);
                uploadFileResp.setVideoUrl(videoUrl);

            }else {
                BeanUtils.copyProperties(video, uploadFileResp);
            }
        }
        return uploadFileResp;
    }

    /**
     * @param originalFilename
     */
    private void checkVideoType(String originalFilename) {
        if (StringUtils.isEmpty(originalFilename)) {
            throw new BusinessException(Result.VIDEO_NOT_FOUND);
        }
        if (!ImageUtils.isVideo(originalFilename)) {
            throw new BusinessException(Result.VIDEO_NOT_VALID_TYPE);
        }
    }

    /**
     * 查询视频表中视频md5是否存在
     *
     * @param md5
     * @return
     */
    private ContVideo getContVideo(String md5) {
        return contVideoMapper.selectOne(Wrappers.<ContVideo>lambdaQuery()
                .eq(ContVideo::getVideoMd5, md5)
                .select(ContVideo::getTitle, ContVideo::getVideoUrl, ContVideo::getVideoMd5, ContVideo::getPictureUrl).last("limit 1"));
    }

    /**
     * 视频发布接口
     *
     * @param contVideoParamList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean addContVideo(List<ContVideoParam> contVideoParamList) {
        //检查置顶时间标识
        boolean topFlag = false;
        List<ContVideo> contVideoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(contVideoParamList)) {
            throw new BusinessException(Result.HTTP_STATUS_METHOD_PARAM_ERROR.getCode(), "添加视频集合不能为空");
        }
        for (ContVideoParam contVideoParam : contVideoParamList) {
            //检测置顶时间标识
            topFlag = checkTop(contVideoParam, SystemConstant.ADD);
            ContVideo contVideo = BeanCopierUtil.copyBean(contVideoParam, ContVideo.class);
            contVideo.setAuditStatus(VideoConstant.AUDIT_STATUS_PASS);
            contVideoList.add(contVideo);
        }

        //保存数据
        Boolean flag = this.saveBatch(contVideoList);
        //更新用户表中作品数 TODO 重点
        Map<Integer, Long> collect = contVideoList.stream().collect(Collectors.groupingBy(ContVideo::getPublisher, Collectors.counting()));
        ucUserService.videoCountIncrease(collect);
        if (topFlag) {
            contVideoList.stream().filter(contVideo -> contVideo.getTopStatus() == VideoConstant.TOP_STATUS_YES).forEach(contVideo ->
                    //添加延迟队列
                    createTopVideoMessage(contVideo.getVideoId(), contVideo.getTopTime()));
        }
        return flag;
    }

    /**
     * 检测是置顶视频时间
     *
     * @param contVideoParam
     * @param type           操作类型 1新增 2 修改
     */
    private Boolean checkTop(ContVideoParam contVideoParam, Integer type) {
        boolean flag = false;
        if (contVideoParam.getTopStatus() == VideoConstant.TOP_STATUS_YES && !ObjectUtils.isEmpty(contVideoParam.getTopTime())) {
            long periodDays = DateUtil.periodDays(LocalDate.now(), contVideoParam.getTopTime());
            if (periodDays <= 0 && Objects.equals(type, SystemConstant.ADD)) {
                throw new BusinessException(Result.VIDEO_TOP_TIME_CHECK.getCode(), "视频标题为：" + contVideoParam.getTitle() + "的视频，置顶结束时间必须为未来某天");
            }
            if (periodDays <= 0 && Objects.equals(type, SystemConstant.UPDATE)) {
                throw new BusinessException(Result.VIDEO_TOP_TIME_CHECK.getCode(), "视频ID为：" + contVideoParam.getVideoId() + "的视频，置顶结束时间必须为未来某天");
            }
            flag = true;
        }
        return flag;
    }

    /**
     * 创建MQ消息
     *
     * @param videoId
     * @param topTime
     */
    private void createTopVideoMessage(Integer videoId, LocalDate topTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(topTime, LocalTime.of(0, 0, 0));
        long endTime = end.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
        long delayTime = Duration.between(now, end).toMillis();
        //SendTopVideoMessage message = SendTopVideoMessage.builder().videoId(videoId).topTime(endTime).build();
        //messageProducer.sendTopVideoMessage(message, delayTime);
    }

    /**
     * 删除视频接口
     *
     * @param contVideoParam
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteContVideo(ContVideoParam contVideoParam) {
        ContVideo contVideo = new ContVideo();
        contVideo.setDeleted(SystemConstant.DELETE);
        List<Integer> videoIdList = contVideoParam.getVideoIds().stream().map(Integer::new).distinct().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(videoIdList)) {
            this.update(contVideo, Wrappers.<ContVideo>lambdaQuery().in(ContVideo::getVideoId, videoIdList));
        }
        //统计每个用户下被删除视频数量，以便更新用户表中作品数
        List<ContVideo> contVideoList = contVideoMapper.selectList(Wrappers.<ContVideo>lambdaQuery().in(ContVideo::getVideoId, videoIdList).select(ContVideo::getPublisher));
        //更新用户表中作品数
        Map<Integer, Long> collect = contVideoList.stream().collect(Collectors.groupingBy(ContVideo::getPublisher, Collectors.counting()));
        ucUserService.videoCountReduce(collect);
        //删除该视频下所有评论
        contVideoCommentMapper.update(null, Wrappers.<ContVideoComment>lambdaUpdate()
                .in(ContVideoComment::getVideoId, contVideoList)
                .set(ContVideoComment::getDeleted, SystemConstant.DELETE));
    }


    /**
     * 客户端首页-搜索按钮-视频列表
     *
     * @param queryParam
     * @return
     */
    public Map<String, Object> appQueryVideoList(ContVideoQueryParam queryParam) {
        IPage<ContVideo> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        IPage<ContVideo> contVideoIPage = contVideoMapper.selectPage(page, Wrappers.<ContVideo>lambdaQuery()
                .eq(!ObjectUtils.isEmpty(queryParam.getCateId()), ContVideo::getCateId, queryParam.getCateId())
                .eq(ContVideo::getDeleted, SystemConstant.NORMAL)
                .eq(ContVideo::getAuditStatus, VideoConstant.AUDIT_STATUS_PASS)
                .like(!StringUtils.isEmpty(queryParam.getSearchKey()), ContVideo::getIntroduction, queryParam.getSearchKey())
                .select(ContVideo::getVideoId, ContVideo::getPublisher, ContVideo::getIntroduction, ContVideo::getLikeCount, ContVideo::getCollectCount, ContVideo::getCreateTime)
                .orderByDesc(ContVideo::getVideoId));
        List<ContVideo> records = contVideoIPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageResult.requestSuccessPage(contVideoIPage);
        }
        List<ContVideoAppListResp> contVideoAppListRespList = BeanCopierUtil.copyBeanList(contVideoIPage.getRecords(), ContVideoAppListResp.class);
        //封装状态
        fillStatus(contVideoAppListRespList, queryParam.getUserId());
        return PageResult.requestSuccessPage(contVideoIPage, contVideoAppListRespList);
    }

    /**
     * 封装用户状态
     *
     * @param contVideoAppListRespList
     * @param userId
     */
    private void fillStatus(List<ContVideoAppListResp> contVideoAppListRespList, Integer userId) {
        //批量设置用户信息
        CompletableFuture<Void> videoInfoFuture = CompletableFuture.runAsync(() -> setVideoInfo(contVideoAppListRespList), threadPoolExecutor);
        //批量设置用户信息
        //CompletableFuture<Void> userInfoFuture = CompletableFuture.runAsync(() -> ucUserService.setUserInfo(contVideoAppListRespList), threadPoolExecutor);
        //查找查询用户喜欢列表，设置用户喜欢状态的方法
        //CompletableFuture<Void> likeStatusFuture = CompletableFuture.runAsync(() -> contVideoLikeService.getLikeStatus(contVideoAppListRespList, userId), threadPoolExecutor);
        //查找查询用户收藏列表，设置用户收藏状态的方法
        //CompletableFuture<Void> collectStatusFuture = CompletableFuture.runAsync(() -> contVideoCollectService.getCollectStatus(contVideoAppListRespList, userId), threadPoolExecutor);
        //CompletableFuture.allOf(videoInfoFuture, userInfoFuture, likeStatusFuture, collectStatusFuture).join();
        CompletableFuture.allOf(videoInfoFuture).join();
    }

    private void setVideoInfo(List<ContVideoAppListResp> contVideoAppListRespList) {
        Set<Integer> videoIds = contVideoAppListRespList.stream().map(ContVideoAppListResp::getVideoId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(videoIds)) {
            return;
        }
        List<ContVideo> contVideos = contVideoMapper.selectList(Wrappers.<ContVideo>lambdaQuery()
                .in(ContVideo::getVideoId, videoIds)
                .select(ContVideo::getVideoId, ContVideo::getPictureUrl));
        Map<Integer, String> videoInfoMap = contVideos.stream().collect(Collectors.toMap(ContVideo::getVideoId, ContVideo::getPictureUrl));
        for (ContVideoAppListResp contVideoAppListResp : contVideoAppListRespList) {
            String pictureUrl = videoInfoMap.get(contVideoAppListResp.getVideoId());
            contVideoAppListResp.setPictureUrl(pictureUrl);
        }
    }

    /**
     * 批量获取视频信息通过Md5值
     *
     * @param checkParam
     * @return
     */
    public Map<String, Map<String, Object>> batchCheckMd5(ContVideoParam checkParam) {
        return contVideoMapper.selectVideoByMd5(checkParam.getMd5List());
    }

}
