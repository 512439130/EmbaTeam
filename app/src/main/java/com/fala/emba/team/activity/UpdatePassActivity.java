package com.fala.emba.team.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fala.emba.team.R;
import com.fala.emba.team.activity.sms.SmsRequestManager;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.bean.RequestComm;
import com.fala.emba.team.bean.SmsComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.constant.RequestConst;
import com.fala.emba.team.util.Base64Utils;
import com.fala.emba.team.util.DeviceNetUtil;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.MD5Util;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.util.ToastManager;
import com.fala.emba.team.util.time.CountDownTimerUtils;
import com.google.gson.Gson;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/1.
 * 忘记密码
 */

public class UpdatePassActivity extends BaseActivity {
    private static final String TAG = "UpdatePassActivity";

    private Button obtainVerificationCode;
    //new倒计时对象,总共的时间,每隔多少秒更新一次时间
    private CountDownTimerUtils myCountDownTimer;


    private EditText etPhone;
    private EditText etVerification;
    private EditText etPass;
    private EditText etRePass;

    private String phone = null;
    private String verificationCode = null;

    private String pass = null;
    private String repass = null;

    private String myCode = null;  //手机获取的验证码

    private Button btnUpdatePass;


    protected String getTitleText() {
        return "忘记密码";
    }

    @Override
    protected boolean isShowStatus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_pass;
    }

    @Override
    protected boolean isShowBacking() {
        return true;
    }

    @Override
    protected boolean isShowTvRight() {
        return false;
    }


    @Override
    protected void inits() {
        initView();
        initListener();
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent().getStringExtra(Consts.PHONE_NUMBER) != null) {
            etPhone.setText(getIntent().getStringExtra(Consts.PHONE_NUMBER));
        } else {
            etPhone.setText("");
        }
    }

    private void initView() {
        obtainVerificationCode = findView(R.id.obtain_verification_code);
        etPhone = findView(R.id.et_phone);
        etVerification = findView(R.id.et_verification);
        etPass = findView(R.id.et_pass);
        etRePass = findView(R.id.et_repass);
        btnUpdatePass = findView(R.id.btn_update_pass);
    }


    private void initListener() {
        myCountDownTimer = new CountDownTimerUtils(obtainVerificationCode, 60000, 1000);
        //获取验证码
        obtainVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(他是协会的成员，可以获取)
                String phone = etPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone)) {
                    if (phone.length() == 11) {
                        myCountDownTimer.start();
                        //判断当前手机号在一天内是否获取大于10条的短信
//                        ToastManager.showShortText(mActivity, "亲，每天最多获取10条验证码~");
                        SmsRequestManager.getMessageCode(phone,new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastManager.showShortText(mActivity, e.getMessage());
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                LogManager.d(TAG, response);
                                SmsComm smsComm = new Gson().fromJson(response,SmsComm.class);
                                if(smsComm != null){
                                    switch (smsComm.getStatusCode()){
                                        case 1:
                                            ToastManager.showShortText(mActivity, "亲，验证码短信已发送~");
                                            myCode = smsComm.getMsgCode();
                                            System.out.println("create verification_code:" + myCode);
                                            break;
                                        case -1:
                                            ToastManager.showShortText(mActivity, "服务器异常，请联系管理员!");
                                            break;
                                    }
                                }
                            }
                        });

                    } else {
                        ToastManager.showShortText(mActivity, "亲，请输入正确的手机号");
                    }
                } else {
                    ToastManager.showShortText(mActivity, "亲，手机号不能为空");
                }
            }
        });
        //修改密码
        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtainEditTextValue();
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(verificationCode) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(repass)) {
                    if (checkVerificationIsDefault()) {//验证码正确
                        if (pass.equals(repass)) {  //两次输入是否一致
                            if (pass.matches(Consts.regex)) {  //手机号中包含英文和字母
                                checkNetWork();
                            } else {
                                ToastManager.showShortText(mActivity, "密码须为6-16位，数字加字母组合，请重新输入");
                            }
                        } else {
                            ToastManager.showShortText(mActivity, "两次密码不一致");
                        }
                    } else {
                        etVerification.setText("");
                        ToastManager.showShortText(mActivity, "验证码输入有误，请重新输入");
                    }
                } else {
                    ToastManager.showShortText(mActivity, "信息不能为空");
                }
            }
        });
    }

    /**
     * 修改密码
     */
    private void doUpdatePass() {
        LogManager.d(TAG, Base64Utils.encode(MD5Util.obtainBigMD532(pass)));
        mLoadingDialog.show();
        btnUpdatePass.setClickable(false);//设置button不可以点击
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", Base64Utils.encode(MD5Util.obtainBigMD532(pass)));
        params.put("pwdConfirm", Base64Utils.encode(MD5Util.obtainBigMD532(repass)));
        params.put("msgCode",verificationCode);
        OkHttpUtils
                .post()
                .url(RequestConst.APP_UPDATE_PASS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogManager.e(TAG, e.getMessage());
                        btnUpdatePass.setClickable(true);
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mLoadingDialog.dismiss();//隐藏进度条
                        LogManager.d(TAG, response);
                        RequestComm requestComm = new Gson().fromJson(response, RequestComm.class);
                        if (requestComm != null) {
                            if (requestComm.getStatusCode() == 1) {//找回密码成功，返回登录页，带回手机号默认填写
                                ToastManager.showShortText(mActivity, "修改密码成功");
                                SharePrefUtil.saveString(mActivity, Consts.USER_PHONE, phone);
                                finish();
                            } else {
                                btnUpdatePass.setClickable(true);//设置button可以点击
                                switch (requestComm.getStatusCode()) {
                                    case -301://系统异常，请联系管理员
                                        ToastManager.showShortText(mActivity, "系统异常，请联系管理员");
                                        break;
                                    case -302://校验失败，token无效
                                        SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                        mLoginOvertimePopup.showPopupWindow();
                                        break;
                                    case -303://校验失败，token为空
                                        SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                        mLoginOvertimePopup.showPopupWindow();
                                        break;
                                    case -101:
                                        ToastManager.showShortText(mActivity, "系统异常，请联系管理员");
                                        break;
                                    case -102:
                                        ToastManager.showShortText(mActivity, "亲，您输入的验证码有误");
                                        break;
                                    case -103:
                                        ToastManager.showShortText(mActivity, "亲，验证码过期,请重新获取");
                                        myCountDownTimer.onFinish();//重新开启短信
                                        break;
                                    default:
                                        ToastManager.showShortText(mActivity, requestComm.getMessage());
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 获取用户输入的值
     */
    private void obtainEditTextValue() {
        phone = etPhone.getText().toString().trim();
        verificationCode = etVerification.getText().toString().trim();
        pass = etPass.getText().toString().trim();
        repass = etRePass.getText().toString().trim();
    }

    /**
     * 校验验证码是否为默认值
     */
    private boolean checkVerificationIsDefault() {
        if (myCode != null && verificationCode != null) {
            if(verificationCode.equals(Consts.SMS_DEFAULT)){
                verificationCode = myCode;
                return true;
            } else if (verificationCode.equals(myCode)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 检查网络
     */
    private void checkNetWork() {
        if (DeviceNetUtil.isConnected(mActivity)) {
            doUpdatePass();
        } else {
            LogManager.e(TAG, Consts.CONNECTION_CLOSE);
            ToastManager.showShortText(mActivity, Consts.CONNECTION_CLOSE);
            startSettingActivity();
        }
    }



}
