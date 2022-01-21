package com.niulijie.springboot.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author niuli
 */
@Setter
@Getter
public class PostQPCodeResponse implements Serializable {

    /**
     * 场馆内外标识 0 内 1 外
     */
    private Integer postStatus;

    /**
     * 场馆类别码
     */
    private String locTypeCode;

    /**
     * 场馆类型名称
     */
    private String locTypeName;

    /**
     * 场馆编号
     */
    private String locNum;

    /**
     * 场馆名称
     */
    private String locName;

    /**
     * 岗位码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 二维码类型 3-警力上报
     */
    private Integer type = 3;

    /**
     * 1 2 3级
     */
    private Integer postLevel;
}
