package com.niulijie.ucenter.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niulijie.ucenter.exception.CommonException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author dcs
 * @desc 校验工具
 */
public class ValidatorUtils {
  private static Validator validator;

  static {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  /**
   * 校验对象
   *
   * @param object 待校验对象
   * @param groups 待校验的组,无则参数全检验
   * @throws CommonException 校验不通过，则报ControllerException异常
   */
  public static void validateEntity(Object object, Class<?>... groups) throws Exception {
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
    String[] errorMsgs = new String[constraintViolations.size()];
    if (!constraintViolations.isEmpty()) {
      int index = 0;
      for (ConstraintViolation<Object> constraint : constraintViolations) {
        errorMsgs[index] = constraint.getMessage();
        index++;
      }
      ObjectMapper mapper =
          new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      // throw new CommonException(mapper.writeValueAsString(errorMsgs));
      throw new CommonException(errorMsgs[0]);
    }
  }

  /**
   * 获取子校验组
   *
   * @param subClass 子组所属类Class
   * @param parentGroup 父类组
   * @return 该类中的属于该父类组的子组
   */
  public static Class[] getGroup(Class subClass, Class parentGroup) {
    Class[] clazzs = subClass.getClasses();
    if (clazzs != null && clazzs.length != 0) {
      List<Class> cs = new ArrayList<>();
      for (Class c : clazzs) {
        if (parentGroup.isAssignableFrom(c)) cs.add(c);
      }
      return cs.toArray(new Class[cs.size()]);
    }
    return null;
  }

  /**
   * 判断对象所有属性是否为空
   * @param object
   * @return true为空，false为非空
   */
  public static boolean checkObjAllFieldsIsNull(Object object) {
    if (null == object) {
      return true;
    }

    try {
      for (Field f : object.getClass().getDeclaredFields()) {
        f.setAccessible(true);

        if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
          return false;
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }

}
