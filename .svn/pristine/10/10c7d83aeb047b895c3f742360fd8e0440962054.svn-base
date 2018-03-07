package com.fala.emba.team.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.constant.RequestConst;
import com.fala.emba.team.util.Base64Utils;
import com.fala.emba.team.util.DeviceNetUtil;
import com.fala.emba.team.util.IdUtil;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.MD5Util;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.util.ToastManager;
import com.google.gson.Gson;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;

/**
 *
 * @author Administrator
 * @date 2017/11/30
 * 登录页面
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private EditText etLoginPhone;
    private EditText etLoginPass;
    private String loginPhone = null;
    private String loginPass = null;
    private Button btnLogin;
    private TextView userRegister;
    private TextView forgetPassword;
    /**
     * 第三方登录相关
     */
    private ImageView imageViewQq;
    private ImageView imageViewWeChat;
    private ImageView imageViewCancel;
    /**
     * 记录登录失败次数
     */
    private int count = 0;
    private int maxCount = 5;

    private Platform qq;
    private Platform weiBo;
    private Platform weChat;


    @Override
    protected String getTitleText() {
        return null;
    }

    @Override
    protected boolean isShowStatus() {
        return false;
    }

    @Override
    protected boolean isShowBacking() {
        return false;
    }

    @Override
    protected boolean isShowTvRight() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected int setTitleBarColorHex() {
        return Color.parseColor("#00FFFFFF");
    }

    @Override
    protected void inits() {
        initView();
        initShare();
        initListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getShareData();
    }

    private void getShareData() {
        if (SharePrefUtil.getString(mActivity, Consts.USER_PHONE, null) != null) {
            etLoginPhone.setText(SharePrefUtil.getString(mActivity, Consts.USER_PHONE, null));
        } else {
            etLoginPhone.setText("");
        }
    }

    private void initView() {
        etLoginPhone = findView(R.id.et_login_phone);
        etLoginPass = findView(R.id.et_login_pass);
        btnLogin = findView(R.id.btn_login);
        userRegister = findView(R.id.user_register);
        forgetPassword = findView(R.id.forget_password);
        imageViewQq = findView(R.id.iv_qq);
        imageViewWeChat = findView(R.id.iv_wechat);
        imageViewCancel = findView(R.id.iv_cancel);
    }

    private void initListener() {
        //用户注册
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RegisterActivity.class);
            }
        });
        //忘记密码
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword.setTextColor(Color.parseColor("#705E54"));
                count = 0;
                obtainEditTextValue();
                Intent intent = new Intent(mActivity, UpdatePassActivity.class);
                intent.putExtra(Consts.PHONE_NUMBER, loginPhone);
                startActivity(intent);
            }
        });
        //登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainEditTextValue();
                if (!TextUtils.isEmpty(loginPhone) && !TextUtils.isEmpty(loginPass)) {
                    checkNetAndLogin();
                } else {
                    ToastManager.showShortText(mActivity, "账号信息不能为空");
                }
            }
        });
        imageViewQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
                qq.setPlatformActionListener(new PlatformActionListener() {

                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        // TODO Auto-generated method stub
                        arg2.printStackTrace();
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        // TODO Auto-generated method stub
                        //输出所有授权信息
                        arg0.getDb().exportData();
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        // TODO Auto-generated method stub

                    }
                });
                //授权并获取用户信息
                qq.showUser(null);
            }
        });
        imageViewWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
                weChat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        // TODO Auto-generated method stub
                        arg2.printStackTrace();
                        LogManager.e(TAG,"weChat_onError："+arg2.getMessage());
                    }

                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        // TODO Auto-generated method stub
                        //输出所有授权信息
                        arg0.getDb().exportData();
                        LogManager.e(TAG,"weChat_onComplete：");
                    }

                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        // TODO Auto-generated method stub
                        LogManager.e(TAG,"weChat_onCancel：");
                    }
                });
                //授权并获取用户信息
                weChat.showUser(null);
            }
        });
        imageViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    /**
     * 检查网络
     */
    private void checkNetAndLogin() {
        if (DeviceNetUtil.isConnected(mActivity)) {
            doLoginPost();
        } else {
            LogManager.e(TAG, Consts.CONNECTION_CLOSE);
            ToastManager.showShortText(mActivity, Consts.CONNECTION_CLOSE);
            startSettingActivity();
        }
    }


    private void initShare() {
        qq = ShareSDK.getPlatform(QQ.NAME);
        weChat = ShareSDK.getPlatform(Wechat.NAME);
        weiBo = ShareSDK.getPlatform(SinaWeibo.NAME);
        //清空授权状态
        qq.removeAccount(true);
        weChat.removeAccount(true);
        weiBo.removeAccount(true);
    }

    /**
     * 获取用户输入的值
     */
    private void obtainEditTextValue() {
        loginPhone = etLoginPhone.getText().toString().trim();
        loginPass = etLoginPass.getText().toString().trim();
    }

    /**
     * 登录
     */
    private void doLoginPost() {
        mLoadingDialog.show();
        btnLogin.setClickable(false);
        Map<String, String> params = new HashMap<>(16);
        params.put("phone", loginPhone);
        params.put("password", Base64Utils.encode(MD5Util.obtainBigMD532(loginPass)));
        OkHttpUtils
                .post()
                .url(RequestConst.APP_LOGIN)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogManager.e(TAG, e.getMessage());
                        mLoadingDialog.dismiss();
                        btnLogin.setClickable(true);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mLoadingDialog.dismiss();
                        btnLogin.setClickable(true);
                        LogManager.d(TAG, response);
                        UserComm userComm = new Gson().fromJson(response, UserComm.class);
                        if (userComm != null) {
                            switch (userComm.getStatusCode()) {
                                case 1:
                                    //保存账号信息
                                    UserComm.UserBean userBean = userComm.getUser();
                                    String data = new Gson().toJson(userBean);
                                    SharePrefUtil.saveString(mActivity, Consts.USER_INFO, data);
                                    SharePrefUtil.saveBoolean(mActivity, Consts.FIRST_START_APP, true);
                                    SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, true);
                                    SharePrefUtil.saveString(mActivity, Consts.TOKEN, userComm.getToken());
                                    //设置别名
                                    JPushInterface.setAlias(mActivity, Integer.parseInt(IdUtil.getLocalTrmSeqNum().substring(5, 12)), userBean.getPhone());
                                    ToastManager.showShortText(mActivity, "登录成功");
                                    openActivityFinish(TabMainActivity.class);
                                    //清零
                                    count = 0;
                                    break;
                                case -1:
                                    //系统异常
                                    ToastManager.showShortText(mActivity, userComm.getMessage());
                                    break;
                                case -2:
                                    //手机号密码错误
                                    count++;
                                    etLoginPass.setText("");
                                    if (count == maxCount || count > maxCount) {
                                        forgetPassword.setTextColor(Color.parseColor("#EA3535"));
                                        ToastManager.showLongText(mActivity, "亲，点击“忘记密码”可修改密码~");
                                    } else {
                                        ToastManager.showShortText(mActivity, userComm.getMessage());
                                    }
                                    break;
                                case -3:
                                    //手机号密码为空
                                    count++;
                                    if (count == maxCount || count > maxCount) {
                                        forgetPassword.setTextColor(Color.parseColor("#EA3535"));
                                        ToastManager.showLongText(mActivity, "亲，点击“忘记密码”可修改密码~");
                                    } else {
                                        ToastManager.showShortText(mActivity, userComm.getMessage());
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
}


