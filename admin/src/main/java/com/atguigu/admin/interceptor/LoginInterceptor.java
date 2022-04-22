package com.atguigu.admin.interceptor;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查
 * 1、配置好拦截器要拦截哪些请求
 * 2、把这些配置放在容器中
 * -----------------------------------------------------------------------
 * 1、根据当前请求，找到HandlerExecutionChain【可以处理请求的handler以及handler的所有 拦截器】
 * 2、先来顺序执行 所有拦截器的 preHandle方法
 *  ● 1、如果当前拦截器prehandler返回为true。则执行下一个拦截器的preHandle
 *  ● 2、如果当前拦截器返回为false。直接    倒序执行所有已经执行了的拦截器的  afterCompletion；
 * 3、如果任何一个拦截器返回false。直接跳出不执行目标方法
 * 4、所有拦截器都返回True。执行目标方法
 * 5、倒序执行所有拦截器的postHandle方法。
 * 6、前面的步骤有任何异常都会直接倒序触发 afterCompletion
 * 7、页面成功渲染完成以后，也会倒序触发 afterCompletion
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
         log.info("preHandle拦截的请求路径是{}",requestURI);

        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        if(loginUser != null){
            //放行
            return true;
        }
        //拦截住，未登录，重定向跳转到登录页
        //session.setAttribute("msg", "请先登录");
        //response.sendRedirect("/");
        //由于重定向取不出request中东西，故修改成给请求域中放信息
        request.setAttribute("msg", "请先登录");
        request.getRequestDispatcher("/").forward(request, response);
        return false;
    }

    /**
     * 目标方法执行之后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle执行{}",modelAndView);
    }

    /**
     * 页面渲染以后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion执行异常{}",ex);
    }
}
