package com.niulijie.mdm.dto.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户信息查询实体类
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class UserInfoParam {

    public interface PcBlacklistCheck {}

    public interface PcBlacklistInsert {}

    public interface PcBlacklistUpdate {}

    public interface PcBlacklistDelete {}

    public interface AppUserInfo {}

    public interface AppUpdateUserInfo {}

    /**
     * 主键，用户id
     */
    @NotNull(message = "用户id不能为空",groups = {AppUserInfo.class, AppUpdateUserInfo.class,
            PcBlacklistInsert.class, PcBlacklistUpdate.class, PcBlacklistCheck.class})
    private Integer userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 签名
     */
    @NotBlank(message = "签名不能为空",groups = {AppUpdateUserInfo.class})
    private String autograph;

    /**
     * 受限类型（黑名单） 0-不受限、1-不能发视频	2-不能发评论、3-不能发视
     */
    @NotEmpty(message = "受限类型不能为空",groups = {PcBlacklistInsert.class, PcBlacklistUpdate.class})
    private List<Integer> limitType;

    /**
     * 受限结束时间
     */
    @NotNull(message = "受限结束时间不能为空",groups = {PcBlacklistInsert.class})
    private LocalDate limitTime;

    /**
     * 删除黑名单用户ID集合
     */
    @NotEmpty(message = "删除黑名单用户ID集合不能为空",groups = {PcBlacklistDelete.class})
    private List<Integer> userIds;
}
