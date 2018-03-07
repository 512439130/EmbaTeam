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

public class SmsRequestManager {
    public static void getMessageCode(String phone, StringCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("phone",phone);
        OkHttpUtils
                .post()
                .url(RequestConst.APP_GET_MESSAGE_CODE)
                .params(params)
                .build()
                .execute(callback);
    }
}
