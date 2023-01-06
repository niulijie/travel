package com.niulijie.ucenter.pojo.present.VO.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuVO {
    @JsonProperty("menu_id")
    private Long menuId;

    @JsonProperty("menu_name")
    private String name;

    @JsonProperty("menu_url")
    private String url;

    @JsonProperty("menu_type")
    private Byte type;

    @JsonIgnore
    private List<MenuVO> menuItems;
}
