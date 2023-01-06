package com.niulijie.ucenter.controller;

import com.niulijie.ucenter.common.BaseResp;
import com.niulijie.ucenter.common.ResultUtil;
import com.niulijie.ucenter.constant.AuthTokenConst;
import com.niulijie.ucenter.pojo.entity.UserAuthEntity;
import com.niulijie.ucenter.pojo.present.AO.login.LoginFistAO;
import com.niulijie.ucenter.pojo.present.AO.login.LoginSecondAO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginFirstVO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginSecondVO;
import com.niulijie.ucenter.service.LoginService;
import com.niulijie.ucenter.utils.AuthTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录管理
 * @author dcs
 * @createTime 2021.8.25
 */
@RestController
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private AuthTokenUtil authTokenUtil;

    /**
     * 首次登录
     * @param loginFistAO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping(value = "/login", produces = "application/json;charset=utf-8")
    public BaseResp loginFirst(@RequestBody LoginFistAO loginFistAO, HttpServletRequest request, HttpServletResponse response) throws Exception {

        LoginFirstVO loginFirstVO = loginService.loginFirst(loginFistAO);

        UserAuthEntity userAuth = new UserAuthEntity(loginFirstVO);
        String token = authTokenUtil.generateToken(userAuth);

        //设置返回header字段
        setResponseHeaders(token, response);

        return ResultUtil.ok(loginFirstVO);
    }

    /**
     * token刷新
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping(value = "/token/refresh", produces = "application/json;charset=utf-8")
    public BaseResp tokenRefresh(HttpServletResponse response) throws Exception {
        String token = authTokenUtil.getToken();
        String newToken = authTokenUtil.refreshToken(token);

        //设置返回header字段
        setResponseHeaders(newToken, response);

        return ResultUtil.ok("Token已刷新");
    }

    /**
     * 二次登录
     * @param loginSecondAO
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping(value = "/tenant/login", produces = "application/json;charset=utf-8")
    public BaseResp loginSecond(@RequestBody @Valid LoginSecondAO loginSecondAO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginSecondVO loginSecondVO = loginService.loginSecond(loginSecondAO);
        return ResultUtil.ok(loginSecondVO);
    }

    /**
     * 设置返回的token和token的过期时间
     * @param token
     * @param response
     */
    public void setResponseHeaders(String token, HttpServletResponse response){
        response.setHeader(AuthTokenConst.AUTHORIZATION, AuthTokenConst.TOKEN_PREFIX + token);

        //返回token的失效时间
        Date expirationDate = authTokenUtil.getExpirationDateFromToken(token);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response.setHeader(AuthTokenConst.EXPIRATION_DATE, simpleDateFormat.format(expirationDate));
    }
}
