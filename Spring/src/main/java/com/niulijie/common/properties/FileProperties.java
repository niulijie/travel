package com.niulijie.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: alk-vmc-im
 * @Package: com.telecomyt.jwportal.alk.vmc.im
 * @ClassName: FileProperties
 * @Author: zhangkai
 * @Description:
 * @Date: 2020/8/29 14:47
 */
@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {

    private  String path;

}
