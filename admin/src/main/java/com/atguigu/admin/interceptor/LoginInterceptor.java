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
 * -----------------------------------------------------------------------
 * https://blog.csdn.net/heweimingming/article/details/79993591
 * 三.拦截器与Filter的区别
 *      1.相同的是:Spring的拦截器与Servlet的Filter有相似之处，比如二者都是AOP编程思想的体现，都能实现权限检查、日志记录等。
 *      2.不同的是:
 *          2.1 使用范围不同:Filter是Servlet 规范规定的，只能用于 Web 程序中。而拦截器既可以用于 Web 程序，也可以用于 Application、Swing 程序中。
 *          2.2 规范不同:Filter 是在 Servlet 规范中定义的，是 Servlet 容器支持的。而拦截器是在 Spring 容器内的，是 Spring 框架支持的
 *          2.3 使用的资源不同:同其他的代码块一样，拦截器也是一个Spring的组件，归Spring管理，配置在 Spring 文件中，因此能使用Spring里的任何资源、对象，
 *              例如 Service对象、数据源、事务管理等，通过IoC注入到拦截器即可:而Filter 则不能。
 *          2.4 深度不同:Filter 在只在 Servlet 前后起作用。而拦截器能够深入到方法前后、异常抛出前后等，因此拦截器的使用具有更大的弹性。
 *              所以在 Spring 构架的程序中，要优先使用拦截器。
 *
 *      3.具体分析：
 *          3.1 过滤器和拦截器触发时机不一样，过滤器是在请求进入容器后，但请求进入servlet之前进行预处理的。请求结束返回也是，是在servlet处理完后，返回给前端之前。
 *          https://img-blog.csdn.net/20180418181500252?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hld2VpbWluZ21pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70
 *          3.2 拦截器可以获取IOC容器中的各个bean，而过滤器就不行，因为拦截器是spring提供并管理的，spring的功能可以被拦截器使用，在拦截器里注入一个service，
 *              可以调用业务逻辑。而过滤器是JavaEE标准，只需依赖servlet api ，不需要依赖spring。
 *          https://img-blog.csdn.net/20180418181054330?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2hld2VpbWluZ21pbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70
 *          3.3 过滤器的实现基于回调函数。而拦截器（代理模式）的实现基于反射，代理分静态代理和动态代理，动态代理是拦截器的简单实现。
 *      4.何时使用拦截器？何时使用过滤器？
 *          4.1 如果是非spring项目，那么拦截器不能用，只能使用过滤器
 *          4.2 如果是处理controller前后，既可以使用拦截器也可以使用过滤器
 *          4.3 如果是处理dispaterServlet前后，只能使用过滤器
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
