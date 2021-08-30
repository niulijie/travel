package com.niulijie.common.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.niulijie.common.dto.ResultStatus;
import com.niulijie.common.exception.CustomException;
import org.apache.commons.collections4.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.forum.utils
 * @email zhoupengbing@telecomyt.com.cn
 * @description 对象校验器
 * @createTime 2019年07月03日 23:37:00 @Version v1.0
 */
public class BeanValidator {

  private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

  /**
   * 校验方法
   *
   * @param t 校验对象
   * @param groups
   * @param <T>
   * @return 返回错误字段和信息
   */
  public static <T> Map<String, String> validate(T t, Class... groups) {
    Validator validator = validatorFactory.getValidator();
    Set result = validator.validate(t, groups);
    if (result.isEmpty()) {
      return Collections.emptyMap();
    } else {
      LinkedHashMap errors = Maps.newLinkedHashMap();
      Iterator iterator = result.iterator();
      while (iterator.hasNext()) {
        ConstraintViolation violation = (ConstraintViolation) iterator.next();
        errors.put(violation.getPropertyPath().toString(), violation.getMessage());
      }
      return errors;
    }
  }

  /**
   * 校验集合
   *
   * @param collection
   * @return
   */
  public static Map<String, String> validateList(Collection<?> collection) {
    // 校验collection是否为空
    Preconditions.checkNotNull(collection);
    Iterator<?> iterator = collection.iterator();
    Map errors;
    do {
      if (!iterator.hasNext()) {
        return Collections.emptyMap();
      } else {
        Object next = iterator.next();
        errors = validate(next, new Class[0]);
      }
    } while (errors.isEmpty());
    return errors;
  }

  /**
   * 通用的校验
   *
   * @param first
   * @param objects
   * @return
   */
  public static Map<String, String> validateObject(Object first, Object... objects) {
    if (objects != null && objects.length > 0) {
      return validateList(Lists.asList(first, objects));
    } else {
      return validate(first, new Class[0]);
    }
  }

  /**
   * 校验，抛出异常
   *
   * @param first
   * @param objects
   * @return
   */
  public static void check(Object first, Object... objects) throws CustomException {
    Map<String, String> result = validateObject(first);
    if (!MapUtils.isEmpty(result)) {
      while (result.entrySet().iterator().hasNext()) {
        Map.Entry<String, String> next = result.entrySet().iterator().next();
        // 参数信息有误
        throw new CustomException(ResultStatus.FAIL.getErrorCode(), next.getValue());
      }
    }
  }
}
