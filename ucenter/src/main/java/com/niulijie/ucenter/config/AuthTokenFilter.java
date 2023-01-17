package com.niulijie.ucenter.config;

import com.niulijie.ucenter.constant.AuthTokenConst;
import com.niulijie.ucenter.exception.CommonException;
import com.niulijie.ucenter.handler.AuthenticationEntryPointHandler;
import com.niulijie.ucenter.utils.AuthTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 1.过滤器的应用场景
 *  1.1 防止未登录就进入界面
 *  1.2 控制应用编码
 *  1.3 滤敏感词汇等场景
 * 2.为什么用 OncePerRequestFilter
 *  2.1 Spring的OncePerRequestFilter类实际上是一个实现了Filter接口的抽象类。spring对Filter进行了一些封装处理
 *  2.2 OncePerRequestFilter是在一次外部请求中只过滤一次。对于服务器内部之间的forward等请求，不会再次执行过滤方法
 *  2.3 实现Filter接口，也会在一次请求中只过滤一次, 实际上，OncePerRequestFilter是为了兼容不同的web 容器，也就是说其实不是所有的容器都过滤一次
 *  2.4 Servlet版本不同，执行的过程也不同。例如：在Servlet2.3中，Filter会过滤一切请求，包括服务器内部使用forward和<%@ include file=/login.jsp%>的情况，但是在servlet2.4中，Filter默认只会过滤外部请求
 * 3. 多个过滤器可以使用  @Component+@Order(),Order数字越小执行越早
 * 4. 对于本项目没有用@Order()注解来标明顺序，但是在WebSecurityConfig中有定义使用顺序
 * @author byron
 * @date 2021.7.16
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter{

    @Autowired
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    private AuthTokenUtil authTokenUtil;

    //loginByUser接口暂时不用安全认证，等mdm都做了修改，再移除
    private static final String[] excludedEndpoints = new String[] {
            "/user/login",
            "/user/loginByUser",
            "/user/auth/apply"
    };

    @Override
    protected void doFilterInternal (
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, CommonException {

        try {
            String authorization = request.getHeader(AuthTokenConst.AUTHORIZATION);
            if (StringUtils.isEmpty(authorization) || !authorization.startsWith(AuthTokenConst.TOKEN_PREFIX))
                throw new CommonException("token不存在");
            String token = authorization.substring(AuthTokenConst.TOKEN_PREFIX.length());
            if (StringUtils.isEmpty(token))
                throw new CommonException("token为空");
            if (authTokenUtil.isTokenExpired(token))
                throw new CommonException("token已失效");
            Integer accessId = authTokenUtil.getAccessIdFromToken(token);
            Integer tenantId = authTokenUtil.getTenantIdFromToken(token);
            Integer userId = authTokenUtil.getUserIdFromToken(token);
            if (userId != null) {
                request.setAttribute(AuthTokenConst.ATTR_ACCESS_ID, accessId);
                request.setAttribute(AuthTokenConst.ATTR_TENANT_ID, tenantId);
                request.setAttribute(AuthTokenConst.ATTR_USER_ID, userId);
                request.setAttribute(AuthTokenConst.ATTR_DEPT_IDS, authTokenUtil.getDeptIdsFromToken(token));
            } else {
                request.setAttribute(AuthTokenConst.ATTR_ACCESS_ID, null);
                request.setAttribute(AuthTokenConst.ATTR_TENANT_ID, null);
                request.setAttribute(AuthTokenConst.ATTR_USER_ID, null);
                request.setAttribute(AuthTokenConst.ATTR_DEPT_IDS, null);
            }
        } catch (Exception e) {
            AuthenticationServiceException authenticationServiceException = new AuthenticationServiceException(e.getMessage());
            authenticationEntryPointHandler.commence(request, response, authenticationServiceException);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> antPathMatcher.match(e, request.getServletPath()));
    }
}
