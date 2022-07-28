package com.niulijie.mdm.config;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * @author df
 * @date 2019/8/6
 */
@Component
public class HandlerInterceptorConfig implements HandlerInterceptor {

    public static ThreadLocal<String> tokenInfo = new ThreadLocal<>();

    /**
     * 请求token验证
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        tokenInfo.remove();
       /* String token = request.getHeader(RequestConstant.AUTHORIZATION);
        if (StringUtils.isEmpty(token) || RequestConstant.UNDEFINED.equals(token)) {
            throw new CustomException(ResultStatus.http_status_unauthorized.getErrorCode(),"身份验证失败，token为空");
        }
        tokenInfo.set(token);*/
        return true;
    }

}
