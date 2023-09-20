package com.niulijie.jdk8.dto;

import com.niulijie.jdk8.enums.DesensitizationTypeEnum;
import com.niulijie.jdk8.annotation.Desensitization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author niuli
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDesensitization {

    private String userName;

    @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE)
    private String phone;

    @Desensitization(type = DesensitizationTypeEnum.PASSWORD)
    private String password;

    @Desensitization(type = DesensitizationTypeEnum.MY_RULE, startInclude = 0, endExclude = 2)
    private String address;
}
