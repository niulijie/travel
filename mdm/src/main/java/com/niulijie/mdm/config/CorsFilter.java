package com.niulijie.mdm.config;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器跨域配置
 * @author dufa
 * @date 2020-11-24
 */
@Component
public class CorsFilter implements Filter {

    /**
     * 解决实现WebMvcConfigurer配置cors不起作用，在配置拦截器的情况下，使用过滤器配置跨域，过滤器依赖于Servlet容器
     * 拦截器则是独立存在的，spring提供并管理 参考：https://blog.csdn.net/heweimingming/article/details/79993591
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        //  Access-Control-Allow-Origin *号代表所有网站可以跨域资源共享，如果当前字段为*那么Access-Control-Allow-Credentials就不能为true，是否允许发送cookie，
        //  当前后端分离使用token时，可以不设置Access-Control-Allow-Credentials
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,POST");
        // Access-Control-Max-Age 这个响应首部表示 preflight request
        // （预检请求）的返回结果（即 Access-Control-Allow-Methods 和Access-Control-Allow-Headers 提供的信息） 可以被缓存多久。建议设置大一点
        response.setHeader("Access-Control-Max-Age", "86400");
        // Access-Control-Allow-Headers * 当有自定义的请求体头时，且是非简单请求，
        //     请求方法不是GET/HEAD/POST
        //     POST请求的Content-Type并非application/x-www-form-urlencoded, multipart/form-data, 或text/plain
        //     请求设置了自定义的header字段
        // 需要手动设置，比如下面的userid
        response.setHeader("Access-Control-Allow-Headers", "DNT,X-CustomHeader,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,userid,Authorization");
        // 解决http非简单请求，预检OPTIONS，跨域问题，参考：https://www.jianshu.com/p/5cf82f092201
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())){
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }else{
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
