package com.fala.emba.team.constant;

import android.os.Environment;

import com.fala.emba.team.activity.RegisterActivity;
import com.fala.emba.team.util.ToastManager;

/**
 * Created by SoMustYY on 2017/10/4.
 */

public class Consts {
    //系统
    public static final String SYSTEM_FLAG = "SYSTEM_FLAG";


    //用户头像名称
    public static final String USER_IMAGE_NAME = "emba_user_image.png";

    public static final String FILE_PATH = Environment.getExternalStorageDirectory().toString();




    //首行趋紧
    public static final String TIGHT_MAIDEN_FLIGHT = "\u3000\u3000";
    //空一格中文格子
    public static final String SINGLE_SPACE = "\u3000";
    //换行
    public static final String SINGLE_WRAP = "\n";
    //用户通用验证码
    public static final String SMS_DEFAULT = "123456";

    //正则表达式
    public static final String regex = "^(?![^a-zA-Z]+$)(?!\\D+$).{6,16}$";  //正则表达式判断 密码输入

    //Toast提示
    //connection_close
    public static final String CONNECTION_CLOSE = "网络未连接，请连接网络";


    //实体 KEY

    public static final String USER_INFO = "USER_INFO";
    public static final String HISTORY_TIME = "HISTORY_TIME";
    public static final String TOKEN = "TOKEN";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String USER_PHONE = "PHONE";



    //首次打开APP
    public static final String FIRST_START_APP = "FIRST_START_APP";
    public static final String LOGIN_PHONE = "LOGIN_PHONE";

    public static final String PHONE_NUMBER = "020-87590888";



    public static final String INTENT_INT_INDEX = "intent_int_index";


    //webview
    public static final String WEB_URL = "WEB_URL";
    public static final String WEB_ASSOCIATION_ID = "WEB_ASSOCIATION_ID";
    public static final String WEB_ACTIVITY_ID = "WEB_ACTIVITY_ID";

    public static final String WEB_PHONE = "WEB_PHONE";
    public static final String WEB_TITLE_NAME = "WEB_TITLE_NAME";


    //push
    public static final String INTENT_FLAG = "INTENT_FLAG";
    public static final String PUSH_FLAG = "PUSH_FLAG";
    public static final String PUSH_HISTORY_FALG = "PUSH_HISTORY_FALG";

    public static final String PUSH_HISTORY_FALG_VALUE = "PUSH_HISTORY_FALG_VALUE";


    //sms
    public static final String SMS_VERIFICATION = "SMS_VERIFICATION";
}
