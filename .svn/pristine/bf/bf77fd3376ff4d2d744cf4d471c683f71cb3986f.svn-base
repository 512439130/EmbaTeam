package com.fala.emba.team.activity.sms;

import com.fala.emba.team.constant.RequestConst;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/11.
 * 短信发送管理
 */

public class SmsManager {
    private static final String TAG = "SmsManager";
    private static String account = "sbaemba@hnlg";
    private static String pswd = "Cb3gFmqn";
    private static String needstatus = "true";
    private static String smsVerificationNumber;
    private static String msg;

    public static void doSendSms( String mobile,String verificationNumber, StringCallback callback){
        smsVerificationNumber = verificationNumber;
        msg = "【EMBA协会通】亲爱的用户，您的验证码为："+ smsVerificationNumber + ",感谢使用！";
        Map<String, String> params = new HashMap<>();
        params.put("account",account);
        params.put("pswd",pswd);
        params.put("mobile",mobile);
        params.put("msg",msg);
        params.put("needstatus",needstatus);
        OkHttpUtils
                .post()
                .url(RequestConst.APP_SEND_SMS)
                .params(params)
                .build()
                .execute(callback);
    }

    /**
     * 获取6位随机数（验证码生成）
     * @return
     */
    public static int obtainSixRandom(){
        return (int)((Math.random()*9+1)*100000);
    }
}
