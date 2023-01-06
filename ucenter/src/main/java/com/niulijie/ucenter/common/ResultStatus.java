package com.niulijie.ucenter.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 错误码
 *
 * @author zhoupengbing
 */
public enum ResultStatus {

    // -1为通用失败
    FAIL(400, "fail"),
    // 0为成功
    SUCCESS(200, "success"),
    USERNAME_OR_PASSWORD_ERROR(403, "用户名或者密码错误"),

    // --- 4xx Client Error ---
    http_status_bad_request(400, "fail"),
    http_status_unauthorized(401, "Unauthorized"),
    http_status_payment_required(402, "Payment Required"),
    http_status_forbidden(403, "Forbidden"),
    http_status_not_found(404, "Not Found"),
    http_status_method_not_allowed(405, "Method Not Allowed"),
    http_status_not_acceptable(406, "Not Acceptable"),
    http_status_proxy_authentication_required(407, "Proxy Authentication Required"),
    http_status_request_timeout(408, "Request Timeout"),
    http_status_conflict(409, "Conflict"),
    http_status_gone(410, "Gone"),
    http_status_length_required(411, "Length Required"),
    http_status_precondition_failed(412, "Precondition Failed"),
    http_status_payload_too_large(413, "Payload Too Large"),
    http_status_uri_too_long(414, "URI Too Long"),
    http_status_unsupported_media_type(415, "Unsupported Media Type"),
    http_status_requested_range_not_satisfiable(416, "Requested range not satisfiable"),
    http_status_expectation_failed(417, "Expectation Failed"),
    http_status_im_a_teapot(418, "I'm a teapot"),
    http_status_unprocessable_entity(422, "Unprocessable Entity"),
    http_status_locked(423, "Locked"),
    http_status_failed_dependency(424, "Failed Dependency"),
    http_status_upgrade_required(426, "Upgrade Required"),
    http_status_precondition_required(428, "Precondition Required"),
    http_status_too_many_requests(429, "Too Many Requests"),
    http_status_request_header_fields_too_large(431, "Request Header Fields Too Large"),

    // --- 5xx Server Error ---
    http_status_internal_server_error(500, "系统错误"),
    http_status_not_implemented(501, "Not Implemented"),
    http_status_bad_gateway(502, "Bad Gateway"),
    http_status_service_unavailable(503, "Service Unavailable"),
    http_status_gateway_timeout(504, "Gateway Timeout"),
    http_status_http_version_not_supported(505, "HTTP Version not supported"),
    http_status_variant_also_negotiates(506, "Variant Also Negotiates"),
    http_status_insufficient_storage(507, "Insufficient Storage"),
    http_status_loop_detected(508, "Loop Detected"),
    http_status_bandwidth_limit_exceeded(509, "Bandwidth Limit Exceeded"),
    http_status_not_extended(510, "Not Extended"),
    http_status_network_authentication_required(511, "Network Authentication Required"),

    // --- 8xx common error ---
    EXCEPTION(800, "exception"),
    INVALID_PARAM(801, "参数有误"),
    USER_NOT_EXITED(802, "用户不存在"),
    USER_EXITED(803, "用户已存在"),
    TENANT_EXISTED(804,"企业已存在"),
    TENANT_MSG_ERROR(805,"企业信息有误"),
    FILE_UPLOAD_ERROR(806,"上传文件失败"),
    FILE_IS_TOO_LARGE(807,"上传的文件太大"),
    FILE_TYPE_ERROR(808,"上传的文件格式不正确"),
    MODEL_IS_NOT_LATEST(809,"请下载最新的模板"),
    ROOT_DEPT_CAN_NOT_DELETE(810,"企业根部门不能删除"),
    DEPT_EXITED(811,"部门名称重复"),
    DEPT_PATH_ERROR(812,"组织机构信息有误"),
    ATTR_IS_NULL(813,"%s为空"),
    ATTR_RULE_ERROR(814,"%s格式不正确"),
    DEPT_PARENT_IS_DISABLE(815,"上级部门是停用状态"),
    DEPT_IS_DELETED(816,"已经删除的部门不能添加用户"),
    PARENT_DEPT_NOT_DELETED_EXITED(817,"上级组织机构不存在，请先添加上级组织机构"),
    EXCEL_NOT_ALLOWED(818,"请使用合法的excel"),
    CONTENT_TOO_LONG(819,"%s内容超过长度限制，请核实"),

    //授权信息校验异常
    LICENSE_RETURN_NULL(820,"调用授权服务返回异常"),
    LICENSE_NOT_ENABLE(821,"授权服务用户中心授权未启用"),
    LICENSE_NOT_VERIFY(822,"授权服务用户中心授权不合法"),
    LICENSE_IS_EXPIRED(823,"授权服务用户中心授权已经过期"),
    LICENSE_ACTIVATION_TOO_LARGE(824,"授权认证失败：激活用户数%s，超过授权数%s"),
    LICENSE_AGGREGATE_TOO_LARGE(825,"授权认证失败：累计注册用户数量%s，超过授权数%s"),

    //文件内容为空(用户数据导入用)
    error_import_dept_empty(20,"文件内容为空，请核对无误后重新上传"),

    //1000以内是系统错误，
    no_login(1000,"没有登录"),
    config_error(1001,"参数配置表错误"),
    user_exist(1002,"用户名已存在"),
    userpwd_not_exist(1003,"用户名不存在或者密码错误"),
    user_number_not_exist(1004,"警号不存在"),
    user_id_card_not_exist(1005,"身份证号不存在");

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultStatus.class);

    private int code;
    private String msg;

    ResultStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static int getCode(String define) {
        try {
            return ResultStatus.valueOf(define).code;
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined error code: {}", define);
            return FAIL.getErrorCode();
        }
    }

    public static String getMsg(String define) {
        try {
            return ResultStatus.valueOf(define).msg;
        } catch (IllegalArgumentException e) {
            LOGGER.error("undefined error code: {}", define);
            return FAIL.getErrorMsg();
        }

    }

    public static String getMsg(int code) {
        for (ResultStatus err : ResultStatus.values()) {
            if (err.code == code) {
                return err.msg;
            }
        }
        return "errorCode not defined ";
    }

    public int getErrorCode() {
        return code;
    }

    public String getErrorMsg() {
        return msg;
    }

}

