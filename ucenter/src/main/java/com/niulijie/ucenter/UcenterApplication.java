package com.niulijie.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.niulijie.ucenter.constant.SystemConstant.ZONE_TIME;

@SpringBootApplication
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }

    /**
     * 配置标准 东八区时区
     */
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZONE_TIME));
    }

}
