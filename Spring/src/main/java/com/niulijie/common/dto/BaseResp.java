package com.niulijie.common.dto;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.dto
 * @email zhoupengbing@telecomyt.com.cn
 * @description 统一返回值
 * @createTime 2019年12月09日 11:02:00 @Version v1.0.0
 */
public class BaseResp<T> {
  /** 返回码 */
  private int code;

  /** 返回信息描述 */
  private String message;

  /** 返回数据 */
  private T data;

  private long currentTime;

  public BaseResp() {}

  /**
   * @param code 错误码
   * @param message 信息
   * @param data 数据
   */
  public BaseResp(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
    this.currentTime = System.currentTimeMillis();
  }

  /**
   * 不带数据的返回结果
   *
   * @param resultStatus
   */
  public BaseResp(ResultStatus resultStatus) {
    this.code = resultStatus.getErrorCode();
    this.message = resultStatus.getErrorMsg();
    this.data = data;
    this.currentTime = System.currentTimeMillis();
  }

  /**
   * 带数据的返回结果
   *
   * @param resultStatus
   * @param data
   */
  public BaseResp(ResultStatus resultStatus, T data) {
    this.code = resultStatus.getErrorCode();
    this.message = resultStatus.getErrorMsg();
    this.data = data;
    this.currentTime = System.currentTimeMillis();
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public long getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(long currentTime) {
    this.currentTime = currentTime;
  }

  @Override
  public String toString() {
    return "BaseResp{"
        + "code="
        + code
        + ", message='"
        + message
        + '\''
        + ", data="
        + data
        + ", currentTime="
        + currentTime
        + '}';
  }

  public static <T> BaseResp<T> create(int errorCode, String msg, T data) {
    BaseResp<T> baseResp = new BaseResp<>();
    baseResp.setCode(errorCode);
    baseResp.setData(data);
    baseResp.setMessage(msg);
    baseResp.setCurrentTime(System.currentTimeMillis());
    return baseResp ;
  }
}
