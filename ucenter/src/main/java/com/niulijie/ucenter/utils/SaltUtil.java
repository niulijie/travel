package com.niulijie.ucenter.utils;

import com.niulijie.ucenter.exception.CommonException;
import com.niulijie.ucenter.menus.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author dcs
 * @desc 密码加盐加密
 */
@Component
public class SaltUtil {

  private static Logger logger = LoggerFactory.getLogger(SaltUtil.class);

  public static String getSalt() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static String encryptionPwd(String pwd, String salt) throws Exception {
    try {
      return DigestUtils.md5DigestAsHex((pwd + salt).getBytes("UTF-8"));
    } catch (Exception e) {
      logger.error("Encryption Password Failed:", e.getMessage());
      throw new CommonException(ErrorEnum.COMMON_ERROR);
    }
  }
}
