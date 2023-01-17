package com.niulijie.ucenter.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.niulijie.ucenter.menus.ErrorEnum;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * @author dcs
 * @date 2022.7.4
 */

@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setStatus(200);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        OutputStream out = response.getOutputStream();

        JSONObject re_json = JSONUtil.createObj();
        re_json.putOpt("code", ErrorEnum.TOKEN_NOT_EXIST_OR_EXPIRED.getCode());
        re_json.putOpt("message", e.getMessage());

        out.write(re_json.toString().getBytes("UTF-8"));
        out.flush();
    }
}
