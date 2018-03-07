package com.fala.emba.team.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import com.fala.emba.team.R;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.bean.PushCustomData;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.ToastManager;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by SoMustYY on 2017/8/30.
 * 推送详情界
 */

public class PushResultActivity extends BaseActivity {
    private static final String TAG = "PushResultActivity：";
    //push
    private TextView pushTitle;
    private TextView pushContent;
    private TextView pushTime;

    private String customContentString;
    private PushCustomData pushCustomData;


    @Override
    protected String getTitleText() {
        return "公告详情";
    }

    @Override
    protected boolean isShowStatus() {
        return true;
    }

    @Override
    protected boolean isShowTvRight() {
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_push_result;
    }

    protected void inits() {
        initView();
        getData();
        initData();
    }
    private void initView() {
        pushTitle = findView(R.id.push_title);
        pushContent = findView(R.id.push_content);
        pushTime = findView(R.id.push_time);
    }

    private void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if(intent.getStringExtra(Consts.INTENT_FLAG).equals(Consts.PUSH_FLAG)){
                Bundle bundle = intent.getExtras();
                customContentString = bundle.getString(JPushInterface.EXTRA_EXTRA);   //通知的额外
                pushCustomData = new Gson().fromJson(customContentString,PushCustomData.class);
                LogManager.d(TAG, "PUSH-customContentString:" + customContentString);
            }else if(intent.getStringExtra(Consts.INTENT_FLAG).equals(Consts.PUSH_HISTORY_FALG)){
                customContentString = intent.getStringExtra(Consts.PUSH_HISTORY_FALG_VALUE);  //通知的额外
                pushCustomData = new Gson().fromJson(customContentString,PushCustomData.class);
                LogManager.d(TAG, "PUSH_HISTORY-customContentString:" + customContentString);
            }
        }
    }

    private void initData() {
        //String content = "广东省气象局于07月17日16时17分发布暴雨黄色预警信号，请注意防御。";
        if (pushCustomData != null) {
            pushTitle.setText(pushCustomData.getTitle());
            pushContent.setText(pushCustomData.getContent());
            pushTime.setText(pushCustomData.getSendTime());
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        ToastManager.cancel();
    }


}
