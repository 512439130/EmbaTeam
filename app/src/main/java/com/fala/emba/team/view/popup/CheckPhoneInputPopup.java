package com.fala.emba.team.view.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.fala.emba.team.R;
import com.fala.emba.team.util.ToastManager;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/18.
 * 校验身份
 */
public class CheckPhoneInputPopup extends BasePopupWindow implements View.OnClickListener {
    private Button mConfirm;
    private EditText mInputEdittext;
    private TextView popTitle;
    private String title;
    private ImageView iv_close;


    public interface CallBack {
        void confirm(String number);
    }

    private CallBack mCallBack;

    public CheckPhoneInputPopup(Activity context, CallBack callBack, String title) {
        super(context);
        this.mCallBack = callBack;
        this.title = title;
        mConfirm = (Button) findViewById(R.id.btn_confirm);
        mInputEdittext = (EditText) findViewById(R.id.ed_input);
        popTitle = (TextView) findViewById(R.id.pop_title);
        popTitle.setText(title);
        iv_close = (ImageView) findViewById(R.id.id_close);
        setAutoShowInputMethod(true);  //自动弹出输入法
        setDismissWhenTouchOuside(true);  //点击屏幕外是否dismiss
        setBackPressEnable(true);  //返回键是否可以取消掉PopupWindow
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    private void bindEvent() {
        mConfirm.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }

    //=============================================================super methods


    @Override
    public Animator initShowAnimator() {
        return getDefaultSlideFromBottomAnimationSet();
    }

    @Override
    public EditText getInputView() {
        return mInputEdittext;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.popup_input_check_phone, null);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public Animator initExitAnimator() {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();
            if (initAnimaView() != null) {
                set.playTogether(
                        ObjectAnimator.ofFloat(initAnimaView(), "translationY", 0, 250).setDuration(400),
                        ObjectAnimator.ofFloat(initAnimaView(), "alpha", 1, 0.4f).setDuration(250 * 3 / 2));
            }
        }
        return set;
    }

    //=============================================================click event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_confirm:
                String phone = mInputEdittext.getText().toString().trim();
                if (phone.equals("")) {
                    ToastManager.showShortText(getContext(), "手机号不能为空");
                } else if (phone.length() != 11) {
                    ToastManager.showShortText(getContext(), "手机号长度不正确");
                } else if (phone.length() == 11) {
                    mCallBack.confirm(phone);
                    mInputEdittext.setText("");
                    dismiss();
                }
                break;
            case R.id.id_close:
                dismiss();
                break;
            default:
                mInputEdittext.setText("");
                dismiss();
                break;
        }

    }
}
