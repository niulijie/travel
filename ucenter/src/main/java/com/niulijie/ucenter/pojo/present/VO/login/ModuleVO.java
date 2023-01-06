package com.niulijie.ucenter.pojo.present.VO.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModuleVO {
    /** 模块ID */
    @JsonProperty("module_id")
    private Long moduleId;
    /** 所属模块ID */
    @JsonProperty("parent_id")
    private Long parentId;
    /** 模块名称 */
    @JsonProperty("module_name")
    private String name;
    /** 模块URL */
    @JsonProperty("module_url")
    private String url;
    /** 模块排序值 */
    @JsonProperty("module_sort")
    private Integer sort;

    @JsonProperty("sub_module")
    private List<ModuleVO> subModuleViews;

    @JsonProperty("menus")
    private List<MenuVO> menuViews;
}
