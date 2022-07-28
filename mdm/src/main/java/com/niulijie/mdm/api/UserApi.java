package com.niulijie.mdm.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.niulijie.mdm.dto.response.IBatchSetUser;
import com.niulijie.mdm.entity.UcUser;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.mdm.dept.api
 * @email zhoupengbing@telecomyt.com.cn
 * @description
 * @createTime 2020年02月05日 08:36:00 @Version v1.0
 */
@Component
public class UserApi {

    private static UserApi userApi;

    /**
     * 批量设置用户名称
     *
     * @param dataList 需要设置用户名称的数据集合
     */
    public static void batchSetUser(List<? extends IBatchSetUser> dataList) {
        List<Integer> idList = dataList.stream().map(data -> data.batchGetUserId()).collect(Collectors.toList());
        List<UcUser> memberList = new ArrayList<>();
        if (CollectionUtils.isEmpty(memberList)) {
            return;
        }
        dataList.forEach(data -> {
            memberList.stream()
                    .filter(member -> data.batchGetUserId() != null && member.getUserId() == data.batchGetUserId().intValue())
                    .findFirst()
                    .map(member -> {
                        //data.batchSetUser(member);
                        return data;
                    });
        });
    }

    /**
     * 获取当前登录账号的授权用户
     *
     * @return
     */
    public static List<Integer> getAuthUserIdList() {
        cn.hutool.json.JSONArray userIdArray = getAuthInfo().getJSONArray("userIds");
        return JSONUtil.toList(userIdArray, Integer.class);
    }

    /**
     * 获取当前登录账号的授权信息
     *
     * @return
     */
    public static JSONObject getAuthInfo() {
        return new JSONObject();
    }

    /**
     * 是否管理员
     *
     * @return
     */
    public static boolean isAdmin() {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @PostConstruct
    public void init() {
        userApi = this;
    }
}
