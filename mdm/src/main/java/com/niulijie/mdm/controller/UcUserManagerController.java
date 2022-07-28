package com.niulijie.mdm.controller;


import com.niulijie.mdm.dto.param.UserInfoParam;
import com.niulijie.mdm.dto.param.UserInfoQueryParam;
import com.niulijie.mdm.result.BaseResult;
import com.niulijie.mdm.result.ResultUtil;
import com.niulijie.mdm.service.UcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 人员信息表 前端控制器
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@RestController
@RequestMapping("/manage/user")
public class UcUserManagerController {

    @Autowired
    private UcUserService ucUserService;

    /**
     * 用户黑名单-根据身份证号或者手机号查找民警
     */
    @PostMapping("/query/user/by")
    public BaseResult queryUserBy(@RequestBody UserInfoParam userInfoParam) {
        return ResultUtil.ok(ucUserService.queryUserBy(userInfoParam));
    }

    /**
     * 用户黑名单-新增
     */
    @PostMapping("/blacklist/add")
    public BaseResult blacklistAdd(@Validated(UserInfoParam.PcBlacklistInsert.class) @RequestBody UserInfoParam userInfoParam) {
        ucUserService.blacklistAdd(userInfoParam);
        return ResultUtil.ok();
    }

    /**
     * 用户黑名单-列表查询
     */
    @PostMapping("/blacklist/query")
    public BaseResult blacklistQuery(@RequestBody UserInfoQueryParam queryParam) {
        return ResultUtil.ok(ucUserService.blacklistQuery(queryParam));
    }
}

