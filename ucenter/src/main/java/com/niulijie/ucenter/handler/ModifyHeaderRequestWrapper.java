package com.niulijie.ucenter.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * 1.作用
 *  1.1 @RequestBody 这个注解在post请求里，规定参数传递使用application/json 流数据传递（序列化后的json字符串）。
 *  1.2 正因为这个请求体中的流数据，流数据只能读取一次。而我们这次实践案例中，过滤器读取一次，接口还需要读取一次， 如果不整点手法，那么这个流数据明显不够用。
 *  1.3 因此,我们采取了继承HttpServletRequestWrapper,创建BodyReaderHttpServletRequestWrapper来将流数据进行复制存储起来。
 *      无论第一次第二次需要使用到流数据时,都去当前存储起来的body数据里去读取。
 */
public class ModifyHeaderRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 所有参数的集合
     */
    private final Map<String, String> customHeaders;

    public ModifyHeaderRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {

        String headerValue = customHeaders.get(name);

        if (headerValue != null){
            return headerValue;
        }

        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> set = new HashSet<>(customHeaders.keySet());

        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }

        return Collections.enumeration(set);
    }

}
