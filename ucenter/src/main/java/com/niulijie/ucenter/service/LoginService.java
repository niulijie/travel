package com.niulijie.ucenter.service;

import com.niulijie.ucenter.pojo.present.AO.login.LoginFistAO;
import com.niulijie.ucenter.pojo.present.AO.login.LoginSecondAO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginFirstVO;
import com.niulijie.ucenter.pojo.present.VO.login.LoginSecondVO;

public interface LoginService extends org.springframework.security.core.userdetails.UserDetailsService{
    /**
     * 首次登录
     * @param loginFistAO
     * @return
     */
    LoginFirstVO loginFirst(LoginFistAO loginFistAO) throws Exception;

    /**
     * 二次登录
     * @param loginSecondAO
     * @return
     * @throws Exception
     */
    LoginSecondVO loginSecond(LoginSecondAO loginSecondAO) throws Exception;
}
