package com.niulijie.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.mdm.common.dto
 * @email zhoupengbing@telecomyt.com.cn
 * @description 分页信息
 * @createTime 2020年02月01日 21:33:00 @Version v1.0
 */
@Data
public class PageDto implements Serializable {

  private static final long serialVersionUID = -4152721444152979429L;

  private Integer size = 10;

  private Integer current = 1;
}
