package com.niulijie.mdm.constant;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 系统常量
 * @author dufa
 * @date 2020-11-25
 */
public interface UserConstant {

    /**
     * 用户状态 0启用，1停用 2 删除
     */
    int NORMAL = 0;

    /**
     * 用户状态 0启用，1停用 2 删除
     */
    int STOP = 1;

    /**
     * 用户状态 0启用，1停用 2 删除
     */
    int DELETE = 2;

    /**
     * 受限类型（黑名单）默认 0-不受限、1-不能发视频 2-不能发评论、3-不能发现
     */
    Integer LIMIT_TYPE_NO = 0;

    /**
     * 受限类型（黑名单）默认 0-不受限、1-不能发视频 2-不能发评论、3-不能发现
     */
    Integer LIMIT_TYPE_FORBID_VIDEO = 1;

    /**
     * 受限类型（黑名单）默认 0-不受限、1-不能发视频 2-不能发评论、3-不能发现
     */
    Integer LIMIT_TYPE_FORBID_COMMENT = 2;

    /**
     * 受限类型（黑名单）默认 0-不受限、1-不能发视频 2-不能发评论、3-不能发现
     */
    Integer LIMIT_TYPE_FORBID_FIND = 3;

    /**
     * 受限类型集合 0-不受限、1-不能发视频 2-不能发评论、3-不能发现
     */
    List<Integer> LIMIT_TYPE_LIST = Lists.newArrayList(1,2,3);

}
