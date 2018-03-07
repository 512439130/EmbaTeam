package com.fala.emba.team.util;

import android.content.Context;

import com.fala.emba.team.constant.Consts;


/**
 * Created by SoMustYY on 2017/9/28.
 * Token类
 */

public class TokenManager {
    private static final String TAG = "TokenManager:";

    public static String checkToken(Context context) {
        String nowTime = DateUtil.getCurrentDate();  //获取当前系统时间
        String historyTime = SharePrefUtil.getString(context, Consts.HISTORY_TIME, "null");
        if (historyTime.equals("null")) {  //第一次获取token
            LogManager.d(TAG, "Token为空" + historyTime);
            SharePrefUtil.saveString(context, Consts.HISTORY_TIME, nowTime);
            return "token_null";
        } else {
            //比较两个时间的时间差是否有120分钟
            //有则重新请求，无则继续使用之前的Token
            int minuteDifference = Math.abs(DateUtil.minuteBetween(nowTime, historyTime));
            if (minuteDifference > 30) {  //时间超过30分钟后
                LogManager.d(TAG, "Token过期" + historyTime);
                SharePrefUtil.saveString(context, Consts.HISTORY_TIME, nowTime);
                return "token_overtime";
            } else {
                LogManager.d(TAG, "上次请求token时间=" + historyTime);
                LogManager.d(TAG, "本次请求token时间=" + nowTime);
                LogManager.d(TAG, "请求token间隔=" + minuteDifference + "分钟");

                return "token_normal";
            }
        }

    }



}
