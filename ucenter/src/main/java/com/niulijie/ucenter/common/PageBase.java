package com.niulijie.ucenter.common;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PageBase {

    /**
     * 开始的页数
     */
    @Range(min = 1,message = "页码不能低于1")
    private Integer current = 1;

    /**
     * 每页数量
     */
    @Range(min = 1,message = "每页数量不能低于1")
    private Integer size = 10;

}
