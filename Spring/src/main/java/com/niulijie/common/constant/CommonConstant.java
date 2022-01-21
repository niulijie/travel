package com.niulijie.common.constant;

/**
 * @ProjectName: integration
 * @Package: com.telecomyt.integration.constant
 * @ClassName: DatabaseConstant
 * @Description:
 * @Author: zhangkai
 * @CreateDate: 2019/10/30 16:40
 * @UpdateUser:
 * @UpdateDate: 2019/10/30 16:40
 * @UpdateRemark:
 */

public interface CommonConstant {

    Integer SUCCESS = 200;

    /**
     * 活动状态【-1 已删除 0 未开始 1 进行中 2 已结束 】
     */
    Integer DELETE_ACTIVE_STATUS = -1;

    /**
     * 活动状态【-1 已删除 0 未开始 1 进行中 2 已结束 】
     */
    Integer NO_BEGIN_ACTIVE_STATUS = 0;

    /**
     * 活动状态【-1 已删除 0 未开始 1 进行中 2 已结束 】
     */
    Integer IN_ACTIVE_STATUS = 1;

    /**
     * 活动状态【-1 已删除 0 未开始 1 进行中 2 已结束 】
     */
    Integer END_ACTIVE_STATUS = 2;

    /**
     * 场所状态【-1 删除 0 正常 1已结束】
     */
    Integer DELETE_LOC_STATUS = -1;

    /**
     * 场所状态【-1 删除 0 正常 1已结束】
     */
    Integer NORMAL_LOC_STATUS = 0;

    /**
     * 场所状态【-1 删除 0 正常 1已结束】
     */
    Integer END_LOC_STATUS = 1;

    /**
     *  状态【 -1删除 1待发车  2已发车 3已到达 4已返程 5已返程到达 】
     */
    Integer DELETE_MOTORCADE_STATUS = -1;

    /**
     *  车辆状态 【  0正常 1删除 】
     */
    Integer DELETE_SITE_STATUS = 1;

    /**
     *  车辆状态 【  0正常 1删除 】
     */
    Integer NORMAL_SITE_STATUS = 0;

    /**
     *  安检点状态 【  0正常 -1删除 】
     */
    Integer DELETE_SC_STATUS = -1;

    /**
     *  安检点状态 【  0正常 -1删除 】
     */
    Integer NORMAL_SC_STATUS = 0;

    /**
     * 定位设备绑定管理-设备状态 状态 -1 删除 0为未绑定 1为绑定
     */
    Integer DELETE_Locating_STATUS = -1;

    /**
     * 定位设备绑定管理-设备状态 状态 -1 删除 0为未绑定 1为绑定
     */
    Integer Bind_Locating_STATUS = 0;

    /**
     * 定位设备绑定管理-设备状态 状态 -1 删除 0为未绑定 1为绑定
     */
    Integer UnBind_Locating_STATUS = 1;

    /**
     *  车辆类型【  0带道车 1普通车辆 3尾车】
     */
    Integer HEAD_SITE = 0;

    /**
     * 1 上岗  0 下岗
     */
    Integer UP_LOC_STATUS = 1;

    /**
     * 1 上岗  0 下岗
     */
    Integer DOWN_LOC_STATUS = 0;

    /**
     * 岗位状态【0 删除 1 正常】
     */
    Integer POST_DELETE_STATUS = 0;

    /**
     * 岗位状态【0 删除 1 正常】
     */
    Integer POST_NORMAL_STATUS = 1;

    /**
     * 岗位级别【1 1级岗位 2 2级岗位 3 3级岗位】
     */
    Integer POST_LEVEL_ONE = 1;

    /**
     * 岗位级别【1 1级岗位 2 2级岗位 3 3级岗位】
     */
    Integer POST_LEVEL_TWO = 2;

    /**
     * 岗位级别【1 1级岗位 2 2级岗位 3 3级岗位】
     */
    Integer POST_LEVEL_THREE = 3;

    /**
     * 闭环内外标识 【0 不区分 1闭环内 2 闭环外】
     * 岗位级别
     */
    Integer POST_CIRCLE_NO = 0;

    /**
     * 闭环内外标识 【0 不区分 1闭环内 2 闭环外】
     * 岗位级别
     */
    Integer POST_CIRCLE_INNER = 1;

    /**
     * 闭环内外标识 【0 不区分 1闭环内 2 闭环外】
     * 岗位级别
     */
    Integer POST_CIRCLE_OUTER = 2;
}

