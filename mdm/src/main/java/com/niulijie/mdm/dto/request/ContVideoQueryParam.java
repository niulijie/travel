package com.niulijie.mdm.dto.request;

import com.niulijie.mdm.result.PageBase;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 视频列表查询参数实体类
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
public class ContVideoQueryParam extends PageBase {

    public interface ListByCategory {}

    public interface ListByUser {}

    public interface ListById {}
    /**
     * 专栏ID
     */
    @NotNull(message = "专栏分类id不能为空",groups = {ListByCategory.class})
    private Integer cateId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空",groups = {ListByUser.class, ListByCategory.class, ListById.class})
    private Integer userId;

    /**
     * 搜索框内容
     */
    private String searchKey;

    /**
     * 查询部门ID集合
     */
    private List<Integer> deptIdList;

    /**
     * 视频id集合
     */
    //@NotEmpty(message = "视频id集合不能为空", groups = {ContVideoQueryParam.ListById.class})
    private List<Integer> videoIdList;

    /**
     * 删除标识 1正常 2 删除
     */
    private Integer deleted;

    /**
     * 置顶状态  默认1 不置顶 2置顶
     */
    private Integer topStatus;
}
