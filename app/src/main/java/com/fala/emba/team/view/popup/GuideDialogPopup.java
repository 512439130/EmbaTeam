package com.fala.emba.team.view.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.view.popup.interpolator.OverShootInterpolator;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by SoMustYY on 2017/11/01.
 * dialog_popup
 */
public class GuideDialogPopup extends BasePopupWindow implements View.OnClickListener{

    private TextView tvSubmit;
    private TextView tvCancel;


    public interface DialogCallBack{
        void submit();
        void cancel();
    }
    private DialogCallBack mDialogCallBack;

    public GuideDialogPopup(Activity context, DialogCallBack dialogCallBack) {
        super(context);
        this.mDialogCallBack = dialogCallBack;
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        setCanBack(true);
        setViewClickListener(this, tvSubmit, tvCancel);
        setDismissWhenTouchOuside(true);  //点击屏幕外是否dismiss
        setBackPressEnable(true);  //返回键是否可以取消掉PopupWindow
        setFullScreen();//全屏问题

    }

    @Override
    public Animator initShowAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, "translationY", 250, 0).setDuration(600);
        transAnimator.setInterpolator(new OverShootInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, "alpha", 0.4f, 1).setDuration(250 * 3 / 2);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }
    @Override
    protected Animator initExitAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, "translationY", 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OverShootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, "alpha", 1f, 0).setDuration(400);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.guide_popup_dialog);
    }
    public void setSubmitText(String text){
        tvSubmit.setText(text);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_submit:
                mDialogCallBack.submit();
                break;
            case R.id.tv_cancel:
                mDialogCallBack.cancel();
                break;
            default:
                break;
        }

    }
}
