package com.niulijie.common.intercepter;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.mdm.common.intercepter
 * @email zhoupengbing@telecomyt.com.cn
 * @description
 * @createTime 2020年03月04日 15:30:00 @Version v1.0
 */
public class AccessInterceptor extends HandlerInterceptorAdapter {

  public static ThreadLocal<String> accessToken = new ThreadLocal<>();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String authorization = request.getHeader("Authorization");
    if (StringUtils.isEmpty(authorization)) {
      // throw new CustomException(ResultStatus.http_status_unauthorized);
    }
    //手动清除token
    accessToken.remove();
    // 设置请求头
    accessToken.set(authorization);
    return true;
  }


}
