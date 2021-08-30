package com.niulijie.common.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.conf
 * @email zhoupengbing@telecomyt.com.cn
 * @description mybatis-plus 配置类
 * @createTime 2019年07月03日 21:22:00 @Version v1.0
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     * 分页插件
     *
     * @return com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
     * @author zhoupengbing
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 最大单页限制数量[默认500] -1不限制
        paginationInterceptor.setLimit(-1);
        return paginationInterceptor;
    }
}
