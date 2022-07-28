package com.niulijie.mdm.dto.response;

import java.io.Serializable;

/**
 * @Description 批量给用户名赋值接口
 * @Auth zhangyong
 * @Date 2021/12/14 14:33
 */
public interface IBatchSetUser extends Serializable {
    /**
     * 获取用户id
     * @return
     */
    Integer batchGetUserId();

    /**
     * 设置用户信息的方法
     * @param userDetailResp
     */
    void batchSetUser(UserDetailResp userDetailResp);

}
