package com.fala.emba.team.constant;

/**
 *
 * @author SoMustYY
 * @date 2017/12/1
 * 请求接口管理类
 */

public class RequestConst {
//    /**
//     * 机器操作系统
//     */
//    public static final String OS = "android";
    /**
     * 配置文件的名称（Sharepreference）
     */
    public static final String SP_XML_NAME = "fala_emba";
    /**
     * 测试服务器
     */
    private static final String APP_TEST_NAME = "http://wms.fala.cn";
    /**
     * 正式服务器
     */
    public static final String APP_REALM_NAME = "http://emba.falacloud.com:8801";
    /**
     * 模块
     */
    private static final String APP_MODULE = "/emba/api/";
    /**
     * 测试接口地址
     */
    public static final String APP_SERVER = APP_TEST_NAME + APP_MODULE;
    //正式接口地址
//    public static final String APP_SERVER = APP_REALM_NAME + APP_MODULE;



    //功能接口(post)
    /**
     * 登录
     */
    public static final String APP_LOGIN = APP_SERVER + "login?";
    /**
     * 注册
     */
    public static final String APP_REGISTER = APP_SERVER + "register?";
    /**
     * 编辑个人信息
     */
    public static final String APP_EDIT_USER = APP_SERVER + "editUser?";
    /**
     * 编辑(更改)手机号
     */
    public static final String APP_CHANGE_PHONE = APP_SERVER + "changePhone?";
    /**
     * 修改密码
     */
    public static final String APP_UPDATE_PASS = APP_SERVER + "changePassword?";
    /**
     * 上传头像
     */
    public static final String APP_UPLOAD_IMAGE = APP_SERVER + "uploadUserHead?";
    /**
     * 获取最新个人信息
     */
    public static final String APP_GET_USER_INFO = APP_SERVER + "userInfo?";
    /**
     * 获取协会信息
     */
    public static final String APP_GET_ASSOCIATION_LIST = APP_SERVER + "associationList?";
    /**
     * 申请入会
     */
    public static final String APP_APPLY_MEMBERSHIP = APP_SERVER + "applyMembership?";
    /**
     * 获取活动信息
     */
    public static final String APP_GET_ACTIVITY_LIST = APP_SERVER + "activityList?";
    /**
     * 活动报名
     */
    public static final String APP_APPLY_ACTIVITY = APP_SERVER + "applyActivity?";
    /**
     * 历史推送列表
     */
    public static final String APP_NOTICE_HISTORY = APP_SERVER + "noticeHistory?";
    /**
     * 发送短信接口（暂时写在客户端）
     */
    public static final String APP_SEND_SMS = "http://120.77.49.16/sms/HttpSendSM?";
    /**
     * 获取短信验证码
     */
    public static final String APP_GET_MESSAGE_CODE = APP_SERVER + "getMessageCode?";

}
