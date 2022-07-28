package com.niulijie.mdm.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageBase {

    /**
     * 开始的页数
     */
    private Integer current = 1;

    /**
     * 每页数量
     */
    private Integer size = 10;
}
