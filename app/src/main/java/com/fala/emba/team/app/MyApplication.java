package com.fala.emba.team.app;

import android.app.Activity;
import android.app.Application;
import android.os.StrictMode;
import android.widget.ImageView;


import com.bumptech.glide.RequestManager;
import com.fala.emba.team.util.AnimationUtil;
import com.fala.emba.team.util.ImageLoaderUtils;
import com.fala.emba.team.util.LogManager;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.mob.MobSDK;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.https.HttpsUtils;
import com.yy.http.okhttp.log.LoggerInterceptor;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by SoMustYY on 2017/7/4.
 *
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;// 全局变量

    //cookie
    private ClearableCookieJar cookieJar;
    public ClearableCookieJar getCookieJar() {
        return cookieJar;
    }
    public void setCookieJar(ClearableCookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }
    /**
     * 获取application实例
     *
     * @return
     */
    public static synchronized MyApplication getInstance() {
        return myApplication;
    }


    @Override
    public void onCreate() {

        super.onCreate();


        //application
        myApplication = this;

        //滑动关闭activity
        BGASwipeBackHelper.init(this,null);

        //imageLoader（改用glide）
        ImageLoaderUtils.isShowLog = true; //开启日志打印
        ImageLoaderUtils.initConfiguration(getApplicationContext());  //ImageLoader初始化

        //okhttp持久化管理cookie
        SaveCookie();
        LogManager.LOG_OPEN_DEBUG = true;

        //极光推送
        JPushInterface.setDebugMode(true);    //开启极光推送日志
        JPushInterface.init(this);

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }


    /**
     * 持久化管理cookie
     */
    public void SaveCookie() {
        cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);

//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);


    }


    /**
     * 开启Img图标点击动画
     * @param mImageView ImagevView
     */
    protected static void startImageViewAnimation(ImageView mImageView){
        mImageView.startAnimation(AnimationUtil.ImageViewAnimationBigOrSmall());
    }




}
