package com.eiisys.ipcc.constants;

/**
 * 消息Code常量类
 */
public class MsgConstants {

    public static final String MSG_SUCCESS = "200001";

    // 传参校验相关 begin
    public static final String MSG_PARAM_IS_NULL = "400001";
    public static final String MSG_PARAM_IS_LONG = "400002";
    public static final String MSG_PARMA_IS_SHORT = "400003";
    public static final String MSG_PARAM_TYPE_NOT_MATCH = "400004";
    /** 华为参数不合法 **/
    public static final String MSG_PAAS_PARAM_INVALID = "400005";
    public static final String MSG_IMG_FORMAT_ERR = "400009";

    // 传参校验相关 end

    // service begin
    /** 排他锁错误码 */
    public static final String MSG_METHOD_ANNO_NOT_ADD = "510001";
    public static final String MSG_PARAM_ANNO_NOT_ADD = "510002";
    public static final String MSG_LOCK_CONFLICT = "510003";

    // service end

    // ** fastdfs begin */
    public static final String MSG_FILE_IS_NULL = "530001";
    public static final String MSG_FILE_PATH_IS_NULL = "530002";
    public static final String MSG_FILE_NOT_EXIST = "530003";
    public static final String MSG_FILE_OVER_SIZE = "530004";
    public static final String MSG_FILE_UPLOAD_FAIL = "530005";
    public static final String MSG_FILE_DOWNLOAD_FAIL = "530006";
    public static final String MSG_FILE_DELETE_FAIL = "530007";

    // ** fastdfs end */

    // ** 登录 begin */

    /** 登录 - 程序报错 */
    public static final String MSG_LOGIN_FAILED = "540001";
    /** 坐席未登录或session已过期 */
    public static final String MSG_NOT_LOGIN = "540002";

    /** saas登录 - 程序报错 */
    public static final String MSG_SAAS_LOGIN_FAILED = "540011";
    /** 用户不存在 */
    public static final String MSG_SAAS_USER_NOT_EXIST = "540012";
    /** 用户名密码错误 */
    public static final String MSG_SAAS_PASSWORD_NOT_MATCH = "540013";
    /** 登录太频繁 */
    public static final String MSG_SAAS_LOGIN_FREQUENTLY = "540014";
    /** 临时令牌无效 */
    public static final String MSG_SAAS_TOKEN_ERROR = "540015";
    /** 未绑定坐席 */
    public static final String MSG_AGENT_ID_NULL = "540016";
    /** 未绑定坐席电话 */
    public static final String MSG_AGENT_PHONE_NULL = "540017";

    /** 华为登录 - 程序报错 */
    public static final String MSG_PAAS_LOGIN_FAILED = "540021";
    /** 用户名密码错误 */
    public static final String MSG_PAAS_PASSWORD_NOT_MATCH = "540022";
    /** 坐席已登录 */
    public static final String MSG_PAAS_LOGIN_REPEAT = "540023";
    /** 坐席绑定电话无效 */
    public static final String MSG_PAAS_PHONE_NOT_MATCH = "540024";
    /** 座席已经登录.并且不能被强制踢出 */
    public static final String MSG_PAAS_LOGIN_LOGINED = "540025";
    /** 坐席处于锁定状态 - 密码错误次数太多 */
    public static final String MSG_PAAS_LOGIN_LOCKED = "540026";
    /** 坐席未登录 **/
    public static final String MSG_PAAS_NOT_LOGINED = "540027";
    /** 坐席签出失败 **/
    public static final String MSG_PAAS_LOGOUT_FAILED = "540028";
    /** 坐席通话中，不能签出 **/
    public static final String MSG_PAAS_LOGOUT_WORKING = "540029";
    /** 坐席鉴权出错 **/
    public static final String MSG_PAAS_GUID_ERROR = "540030";
    /** 心跳检测失败 **/
    public static final String MSG_PAAS_HEARTBEAT_ERROR = "540031";
    /** 坐席状态切换失败 **/
    public static final String MSG_PAAS_STATUS_CHANGE_FAILED = "540032";
    /** 无效坐席状态 **/
    public static final String MSG_PAAS_STATUS_ERROR = "540033";

    // ** 登录 end */

    // ** 语音通话相关begin */
    /** 呼出失败 **/
    public static final String MSG_PAAS_CALLOUT_FAILED = "550001";
    /** 外呼号码错误 **/
    public static final String MSG_PAAS_CALLOUT_NUM_ERROR = "550002";
    // ** 语音通话相关end */
}
