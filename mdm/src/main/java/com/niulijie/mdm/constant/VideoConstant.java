package com.niulijie.mdm.constant;

/**
 * 视频常量
 * @author dufa
 * @date 2020-11-25
 */
public interface VideoConstant {

    /**
     * 点赞评论对象：0-短视频、1-评论
     */
    int LIKE_TYPE_VIDEO = 0;

    /**
     * 点赞对象：0-短视频、1-评论
     */
    int LIKE_TYPE_COMMENT = 1;

    /**
     * 置顶状态  默认1 不置顶 2置顶
     */
    int TOP_STATUS_NO = 1;

    /**
     * 置顶状态  默认1 不置顶 2置顶
     */
    int TOP_STATUS_YES = 2;

    /**
     * 评论可见key
     */
    String COMMENT_SHOW_KEY = "comment_show";

    /**
     * 评论可见，默认是
     */
    String COMMENT_SHOW_VALUE = "是";

    /**
     * 是否可评论key
     */
    String COMMENT_ENABLE_KEY = "comment_enable";

    /**
     * 是否可评论，默认是
     */
    String COMMENT_ENABLE_VALUE = "是";

    /**
     * 移动端视频上传按钮是否显示
     */
    String VIDEO_UPLOAD_SHOW_KEY = "upload_show";

    /**
     * 移动端视频上传按钮是否显示，默认是
     */
    String VIDEO_UPLOAD_SHOW_VALUE = "是";

    /**
     * 审核状态 0-待审核、1-通过、2-驳回
     */
    int AUDIT_STATUS_WAIT = 0;

    /**
     * 审核状态 0-待审核、1-通过、2-驳回
     */
    int AUDIT_STATUS_PASS = 1;

    /**
     * 审核状态 0-待审核、1-通过、2-驳回
     */
    int AUDIT_STATUS_REJECT = 2;

    /**
     * 回复状态 1-未回复，2-已回复，默认1
     */
    int ONLINE_FEEDBACK_NO = 1;

    /**
     * 回复状态 1-未回复，2-已回复，默认1
     */
    int ONLINE_FEEDBACK_FINAL = 2;


}
