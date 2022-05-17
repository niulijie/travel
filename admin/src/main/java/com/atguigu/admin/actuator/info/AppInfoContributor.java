package com.atguigu.admin.actuator.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AppInfoContributor implements InfoContributor {

    /**
     *
     * @param builder
     */
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("msg", "你好")
                .withDetail("hello", "dada")
                .withDetails(Collections.singletonMap("world", 666));
    }
}
