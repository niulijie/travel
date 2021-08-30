package com.niulijie.common.api;

import com.niulijie.common.constant.CoreConstant;
import com.niulijie.common.intercepter.AccessInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.mdm.dept.api
 * @email zhoupengbing@telecomyt.com.cn
 * @description 核心API【请求头信息】
 * @createTime 2020年02月05日 09:43:00 @Version v1.0
 */
@Component
public class CoreApi {

    public static ThreadLocal<String> tokenInfo = new ThreadLocal<>();
    private static CoreApi coreApi;

    /**
     * 获取请求头的token信息
     *
     * @return
     */
    public static String getToken() {
        return AccessInterceptor.accessToken.get();
    }

    /**
     * 获取请求头信息
     *
     * @return
     */
    public static Map<String, String> getHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put(CoreConstant.AUTH_HEADER_TOKEN, getToken());
        return headers;
    }

    @PostConstruct
    public void init() {
        coreApi = this;
    }
}
