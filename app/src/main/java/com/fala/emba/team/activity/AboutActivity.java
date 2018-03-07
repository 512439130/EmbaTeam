package com.fala.emba.team.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RelativeLayout;

import com.fala.emba.team.R;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.view.popup.DialogPopup;

/**
 * Created by Administrator on 2017/12/5.
 * 关于
 */
public class AboutActivity extends BaseActivity {
    private RelativeLayout rlContact;
    private DialogPopup dialogPopup;

    @Override
    protected String getTitleText() {
        return "关于";
    }

    @Override
    protected boolean isShowStatus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void inits() {
        initView();
        initListener();
    }
    @Override
    protected boolean isShowTvRight() {
        return false;
    }

    private void initView() {
        rlContact = findView(R.id.about_contact);
        dialogPopup = new DialogPopup(mActivity, new DialogPopup.DialogCallBack() {
            @Override
            public void submit() {
                dialogPopup.dismiss();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Consts.PHONE_NUMBER)));//跳转到拨号界面并携带电话号码
            }
            @Override
            public void cancel() {
                dialogPopup.dismiss();
            }
        });
        dialogPopup.setContentText(Consts.PHONE_NUMBER);
        dialogPopup.setSubmitText("拨打");
    }

    private void initListener() {
        rlContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPopup.showPopupWindow();
            }
        });
    }



}
