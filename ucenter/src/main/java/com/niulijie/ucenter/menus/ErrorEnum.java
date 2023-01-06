package com.niulijie.ucenter.menus;

public enum ErrorEnum {
  COMMON_ERROR(711, "网络拥挤，请重试"),
  PARAM_ERROR(712, "参数异常"),
  TENANT_NOT_EXIST(713, "该企业不存在或已停用"),

  NO_PLATFORM_ADMIN(101, "您暂无平台管理权限"),
  // ACCOUNT_ALREADY_EXIST(102, "该用户已绑定注册"),
  ACCOUNTID_ERROR(102, "用户访问ID错误"),
  USERNAME_ALREADY_EXIST(103, "该用户名已被占用"),
  USERNAME_IS_bINDING(103, "该账号已被绑定，请尝试其他账号名"),
  USER_NOT_EXIST(104, "该用户不存在"),
  ACCOUNT_ALREADY_EXIST(105, "该账号已存在"),
  ACCOUNT_NOT_EXIST(106, "账号不存在或已停用"),
  USERNAME_ERROR(107, "用户名不正确"),
  PASSWORD_ERRPR(108, "密码不正确"),
  LOGIN_ERRPR(109, "账号或密码错误"),
  PROVING_ERRPR(110, "登陆失效,请重新登陆"),
  TOKEN_ERROR(111, "授权码已失效"),
  QR_EXPIRE(112, "二维码已失效"),
  UNAUTHORIZED_LOGIN(113, "暂无权限登陆，请联系管理员"),
  NO_QUERY(114, "您暂无权限查看"),
  ACCOUNT_ALREADY_OPEN(115, "默认账号已存在！"),
  NO_OPERATE(114, "您暂无权限查看"),
  OLD_PASSWORD_ERRPR(108, "原始密码不正确"),
  USER_NO_OR_ACCOUNT_NO(116, "用户不存在或者用户的默认账号不存在"),

  ROLENAME_ALREADY_EXIST(201, "该角色名称已被占用"),
  ROLE_NOT_EXIST(202, "角色不存在或已删除"),
  SYSCODE_NOT_NULL(203, "系统编号不能为空"),
  SYSCODE_ALREADY_EXIST(204, "该系统编号已存在"),
  SYSCODE_NOT_EXIST(205, "系统编号不存在"),

  MENUNAME_ALREADY_EXIST(301, "权限点已存在"),
  MENU_NOT_EXIST(302, "权限点不存在或已删除"),
  MODULE_NOT_EXIST(303, "模块不存在或已删除"),
  MENUID_ERROR(304, "权限菜单ID错误"),
  PARENT_MENU_ERROR(305, "父模块/父菜单不存在或已删除"),
  URL_ALREADY_EXIST(306, "菜单路由已存在"),
  MODULE_ID_CANNOT_ITSELT(307, "权限点所属模块不可为它本身"),

  GROUPNAME_ALREADY_EXIST(601, "所属组织/同级组织中，该组织名称已被占用"),
  GROUP_NOT_EXIST(602, "该组织不存在或已删除"),

  USER_ID_ILLEGAL(701, "用户ID不合法"),
  CONTACT_THE_ADMIN(702, "请联系管理员"),
  TOKEN_NOT_EXIST_OR_EXPIRED(880,"token不存在或已失效"),
  ;

  private int code;
  private String msg;

  ErrorEnum() {}

  ErrorEnum(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
