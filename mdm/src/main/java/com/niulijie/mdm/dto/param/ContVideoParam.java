package com.niulijie.mdm.dto.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 短视频新增，编辑，删除操作类
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class ContVideoParam {

    public interface PcAddContVideo {}

    public interface AppAddContVideo {}

    public interface UpdateContVideo {}

    public interface DeleteContVideo {}

    public interface TopContVideo {}

    public interface PcCheckMd5 {}

    public interface AppCheckMd5 {}

    public interface VideoInfo {}
    /**
     * 主键，视频id
     */
    @NotNull(message = "视频id不能为空",groups = {UpdateContVideo.class, TopContVideo.class, VideoInfo.class})
    private Integer videoId;

    /**
     * 分类id
     */
    @NotNull(message = "分类id不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private Integer cateId;

    /**
     * 视频标题
     */
    @NotBlank(message = "视频标题不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private String title;

    /**
     * 简介
     */
    @NotBlank(message = "简介不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private String introduction;

    /**
     * 视频的云端地址
     */
    @NotBlank(message = "视频的云端地址不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private String videoUrl;

    /**
     * 视频md5
     */
    @NotBlank(message = "视频md5不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class, AppCheckMd5.class})
    private String videoMd5;

    /**
     * 图片的云端地址
     */
    @NotBlank(message = "图片的云端地址不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private String pictureUrl;

    /**
     * 发布者
     */
    @NotNull(message = "发布者不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private Integer publisher;

    /**
     * 视频所属部门id
     */
    @NotNull(message = "视频所属部门id不能为空",groups = {PcAddContVideo.class, AppAddContVideo.class})
    private Integer deptId;

    /**
     * 审核状态 0-待审核、1-通过、2-驳回
     */
    private Integer auditStatus;

    /**
     * 是否可被评论  1 默认 可以 2 不可
     */
    private Integer commented;

    /**
     * 是否可见评论  1 默认 可以  2 不可
     */
    private Integer visualed;

    /**
     * 置顶状态  默认1 不置顶 2置顶
     */
    @NotNull(message = "视频置顶状态不能为空",groups = {PcAddContVideo.class, TopContVideo.class})
    private Integer topStatus;

    /**
     * 置顶结束时间
     */
    private LocalDate topTime;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;

    @NotEmpty(message = "视频id集合不能为空",groups = {DeleteContVideo.class})
    private List<Integer> videoIds;

    @NotEmpty(message = "视频md5集合不能为空",groups = {PcCheckMd5.class})
    private List<String> md5List;
}
