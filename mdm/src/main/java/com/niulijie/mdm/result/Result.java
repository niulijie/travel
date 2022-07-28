package com.niulijie.mdm.result;

import java.io.Serializable;

/**
 * 返回结果封装
 *
 * @author df
 * @date 2019/8/6
 */
public enum Result implements Serializable {

    /*|200 |请求成功(OK)，操作成功类信息码|
     |204 |没有内容，当一个动作成功执行，但没有任何内容可以返回，比如执行查询数据操作成功，但是数据库没有相关数据，可以返回204|
    |400 |请求无效 (Bad request)，参数类错误信息码|
    |401 |请求要求身份验证（Unauthorized）。对于需要token的接口，用户未认证，请求失败 |
    |403 |服务器拒绝请求（Forbidden）token失效或者权限类错误信息码 |
    |500 |服务器遇到错误，无法完成请求（Internal Server Error） |
    |512 |数据操作（增删改）失败类信息错误码|
    |513 |数据操作（查）失败类信息错误码|*/

    // 公共错误码
    SUCCESS(200, "success"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "not_find"),
    REPEAT_REQUEST(405, "Repeat request"),
    ERROR(500, "Internal Server Error"),
    HTTP_STATUS_METHOD_PARAM_ERROR(512, "请求参数有误"),

    // 自定义业务异常码 栏目相关 10100  视频相关 11100 评论相关 12100 审核（权限）相关 13100 反馈意见 14100 敏感词 15100 用户相关 16100
    //专栏错误码
    CATEGORY_DELETE_FAIL(10100, "该专栏下存在视频，无法删除"),
    CATEGORY_NAME_EXIST(10101, "该专栏名称已存在，请重新输入"),

    //视频相关 11100
    VIDEO_NOT_FOUND(10100, "没有获取到文件"),
    VIDEO_NOT_VALID_TYPE(10101, "不是视频文件"),
    VIDEO_TOP_TIME_NULL(16102, "视频置顶结束时间不能为空"),
    VIDEO_TOP_TIME_CHECK(16103, "视频置顶结束时间必须为未来某天"),
    VIDEO_COLLECT_FINAL(16104, "您已收藏过该视频"),
    VIDEO_COMMENT_ENABLE(16105, "当前视频禁止评论"),
    VIDEO_COMMENT_SHOW(16106, "当前视频禁止查看评论"),

    // 评论
    COMMENT_ENABLE_FALSE(12100, "全局评论功能已关闭"),
    COMMENT_SHOW_FALSE(12101, "全局评论显示功能已关闭"),
    COMMENT_DELETE_AUTH(12102, "您没有权力删除该评论"),
    COMMENT_DELETE_FAIL(12103, "未找到可正常删除得评论"),
    COMMENT_LIKE_NULL(12104, "未找到当前评论得点赞信息"),
    COMMENT_LIKE_AUTH(12105, "您没有权力取消该评论点赞"),
    COMMENT_LIKE_FINAL(12106, "您已点赞过该评论"),

    //敏感词 15100
    SENSITIVE_WORD_CHEK(15100,"内容中含有敏感词，请核实"),

    //用户相关
    BLACKLIST_SEARCH_CHEK(16100, "请输入身份证号或手机号精准搜索民警"),
    BLACKLIST_LIMIT_TIME_NULL(16101, "受限结束时间不能为空"),
    BLACKLIST_LIMIT_TIME_CHECK(16102, "受限结束时间必须为未来某天"),
    BLACKLIST_LIMIT_USER(16103, "您已被限制不能发布评论");

    private final Integer code;

    private final String msg;

    Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getCodeMsg(Integer code) {
        for (Result result : values()) {
            if (result.getCode().equals(code)) {
                return result.getMsg();
            }
        }
        return null;
    }


}
