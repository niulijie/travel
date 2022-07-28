package com.niulijie.mdm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niulijie.mdm.constant.LimitTypeEnum;
import com.niulijie.mdm.constant.UserConstant;
import com.niulijie.mdm.dto.param.User;
import com.niulijie.mdm.dto.param.UserInfoParam;
import com.niulijie.mdm.dto.param.UserInfoQueryParam;
import com.niulijie.mdm.dto.response.BlacklistDetailResp;
import com.niulijie.mdm.dto.response.IBatchSetUser;
import com.niulijie.mdm.dto.response.UserDetailResp;
import com.niulijie.mdm.entity.UcUser;
import com.niulijie.mdm.entity.UcUserDept;
import com.niulijie.mdm.mapper.UcUserMapper;
import com.niulijie.mdm.publisher.MessageProducer;
import com.niulijie.mdm.dto.message.SendLimitUserMessage;
import com.niulijie.mdm.publisher.DeptUserEventPublisher;
import com.niulijie.mdm.result.BusinessException;
import com.niulijie.mdm.result.Result;
import com.niulijie.mdm.util.BeanCopierUtil;
import com.niulijie.mdm.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UcUserService extends ServiceImpl<UcUserMapper, UcUser> {

    @Autowired
    private UcUserDeptService ucUserDeptService;

    @Autowired
    private MessageProducer messageProducer;

    /*@Resource
    private RedisTemplate<String, Object> redisTeplate;*/

    @Autowired
    private DeptUserEventPublisher deptUserEventPublisher;

    /**
     * 点赞数统计 +1 操作
     */
    public void likeCountIncrease(Integer publisher) {
        this.baseMapper.likeCountIncrease(publisher);
    }

    /**
     * 点赞数统计 -1 操作
     */
    public void likeCountReduce(Integer publisher) {
        this.baseMapper.likeCountReduce(publisher);
    }

    /**
     * 作品数统计 操作
     */
    public void videoCountIncrease(Map<Integer, Long> publisherList) {
        this.baseMapper.videoCountIncrease(publisherList);
    }

    /**
     * 作品数统计 操作
     */
    public void videoCountReduce(Map<Integer, Long> publisherList) {
        this.baseMapper.videoCountReduce(publisherList);
    }

    /**
     * 查询用户详细信息
     * @param publisherList
     * @return
     */
    public List<UserDetailResp> getUserDetailById(List<Integer> publisherList) {
        return baseMapper.getUserDetailById(publisherList);
    }

    /**
     * 设置用户信息的方法
     * @param dataList
     */
    public void getUserInfo(List<? extends IBatchSetUser> dataList) {
        List<Integer> userIdList = dataList.stream().map(IBatchSetUser::batchGetUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<UserDetailResp> userDetailList = this.getUserDetailById(userIdList);
        if (CollectionUtils.isEmpty(userDetailList)) {
            return;
        }
        dataList.forEach(data -> {
            userDetailList.stream()
                    .filter(userDetailResp -> data.batchGetUserId() != null && userDetailResp.getUserId() == data.batchGetUserId().intValue())
                    .findFirst()
                    .map(userDetailResp -> {
                        data.batchSetUser(userDetailResp);
                        return data;
                    });
        });
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user) {
        UcUser ucUser = BeanCopierUtil.copyBean(user, UcUser.class);
        ucUser.setTelephone(user.getPhone());
        //新增用户
        this.saveOrUpdate(ucUser);
        //新增用户和部门关系
        //先删除再新增
        ucUserDeptService.remove(Wrappers.<UcUserDept>lambdaQuery().eq(UcUserDept::getUserId, user.getUserId()));
        List<Integer> deptIds = user.getDeptIds();
        if(!CollectionUtils.isEmpty(deptIds)){
            List<UcUserDept> ucUserDeptList = new ArrayList<>();
            for (Integer deptId : deptIds) {
                UcUserDept ucUserDept = BeanCopierUtil.copyBean(user, UcUserDept.class);
                ucUserDept.setDeptId(deptId);
                ucUserDeptList.add(ucUserDept);
            }
            ucUserDeptService.saveOrUpdateBatch(ucUserDeptList);
            // 发布 springboot 事件监听器
            deptUserEventPublisher.sendDeptUserEvent(deptIds);
        }
    }

    /**
     * 用户更新
     * @param user
     */
    public void updateUser(User user) {
        this.addUser(user);
    }

    /**
     * 用户删除 - 修改状态为删除状态
     * @param user
     */
    public void deleteUser(User user) {
        //修改用户表状态为删除状态
        UcUser ucUser = BeanCopierUtil.copyBean(user, UcUser.class);
        this.updateById(ucUser);
    }


    /**
     * 用户黑名单-新增
     * @param userInfoParam
     */
    public void blacklistAdd(UserInfoParam userInfoParam) {
        //redisTemplate.expire(RedisKeyConstant.COMMENT_AUDIT,0, TimeUnit.SECONDS);
        if(UserConstant.LIMIT_TYPE_LIST.contains(userInfoParam.getLimitType()) && ObjectUtils.isEmpty(userInfoParam.getLimitTime())){
            throw new BusinessException(Result.BLACKLIST_LIMIT_TIME_NULL);
        }
        long periodDays = DateUtil.periodDays(LocalDate.now(), userInfoParam.getLimitTime());
        if(periodDays <= 0){
            throw new BusinessException(Result.BLACKLIST_LIMIT_TIME_CHECK);
        }
        UcUser ucUser = BeanCopierUtil.copyBean(userInfoParam, UcUser.class);
        String limitType = userInfoParam.getLimitType().stream().map(String::valueOf).collect(Collectors.joining(","));
        ucUser.setLimitType(limitType);
        //修改数据库
        this.baseMapper.updateById(ucUser);
        //添加延迟队列
        createSendLimitUserMessage(ucUser.getUserId(), ucUser.getLimitTime());
        //redisTemplate.expire(RedisKeyConstant.COMMENT_AUDIT,0, TimeUnit.SECONDS);
    }

    /**
     * 创建MQ消息
     * @param userId
     * @param limitTime
     */
    private void createSendLimitUserMessage(Integer userId, LocalDate limitTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(limitTime, LocalTime.of(0, 0, 0));
        long endTime = end.toInstant(ZoneOffset.of("+8")).toEpochMilli() / 1000;
        long delayTime = Duration.between(now, end).toMillis();
        SendLimitUserMessage message = SendLimitUserMessage.builder().userId(userId).limitTime(endTime).build();
        messageProducer.sendLimitUserMessage(message, delayTime);
    }

    /**
     * 用户黑名单-查询
     * @param queryParam
     * @return
     */
    public IPage<BlacklistDetailResp> blacklistQuery(UserInfoQueryParam queryParam) {
        Page<BlacklistDetailResp> page = new Page<>(queryParam.getCurrent(), queryParam.getSize());
        IPage<BlacklistDetailResp> contVideoManagerRespIPage = this.baseMapper.selectPageList(page, queryParam.getSearchKey());
        List<BlacklistDetailResp> blacklistDetailRespList = contVideoManagerRespIPage.getRecords();
        if(!CollectionUtils.isEmpty(blacklistDetailRespList)){
            //设置受限类型名称
            renameLimitName(blacklistDetailRespList);
            //批量设置用户信息
            this.getUserInfo(contVideoManagerRespIPage.getRecords());
        }
        return contVideoManagerRespIPage;
    }

    /**
     * 设置受限类型名称
     * @param blacklistDetailRespList
     */
    private void renameLimitName(List<BlacklistDetailResp> blacklistDetailRespList) {
        for (BlacklistDetailResp blacklistDetailResp : blacklistDetailRespList) {
            List<Integer> codeList = Arrays.stream(blacklistDetailResp.getLimitType().split(",")).map(Integer::valueOf).collect(Collectors.toList());
            String collect = codeList.stream().map(LimitTypeEnum::getName).collect(Collectors.joining("、"));
            blacklistDetailResp.setLimitTypeName(collect);
        }
    }

    /**
     * 用户黑名单-根据身份证号或者手机号查找民警
     * @param userInfoParam
     * @return
     */
    public List<Map<String,Object>> queryUserBy(UserInfoParam userInfoParam) {
        if(StringUtils.isEmpty(userInfoParam.getIdCard()) && StringUtils.isEmpty(userInfoParam.getTelephone())){
            throw new BusinessException(Result.BLACKLIST_SEARCH_CHEK);
        }
        return this.baseMapper.selectMaps(Wrappers.<UcUser>lambdaQuery()
                .eq(!StringUtils.isEmpty(userInfoParam.getIdCard()), UcUser::getIdCard, userInfoParam.getIdCard())
                .eq(!StringUtils.isEmpty(userInfoParam.getTelephone()), UcUser::getTelephone, userInfoParam.getTelephone())
                .eq(UcUser::getStatus, UserConstant.NORMAL)
                //不受限状态
                .apply("FIND_IN_SET(0,limit_type)")
                .select(UcUser::getUserId, UcUser::getName));
    }
}
