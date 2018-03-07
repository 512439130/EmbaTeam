package com.yy.http.okhttp.utils;

import android.util.Log;

/**
 * Created by yy on 15/11/6.
 */
public class L
{
    private static boolean debug = true;  //开关网络请求日志

    public static void e(String msg)
    {
        if (debug)
        {
            Log.e("OkHttp", msg);
        }
    }

}

