package com.fala.emba.team.view.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.util.DateUtil;
import com.fala.emba.team.util.time.CountDownTimerUtils;
import com.fala.emba.team.view.popup.interpolator.OverShootInterpolator;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by 大灯泡 on 2016/10/11.
 * <p>
 * 全屏的popup
 */

public class FullScreenPopup extends BasePopupWindow implements View.OnClickListener {
    private TextView tvSubmit;
    private TextView tvContent;
    private Activity mContext;
    private CallBack mCallBack;


    public FullScreenPopup(Activity context, CallBack callBack) {
        super(context);
        this.mContext = context;
        this.mCallBack = callBack;
        inits();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                mCallBack.submit();
                break;
        }
    }

    private void inits() {
        tvSubmit = (TextView) findViewById(R.id.tv_submit);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.setText("您的账号与" + DateUtil.getCurrentDate() + "在其他设备上登录。如果这不是您的操作，您的账号密码已经泄露，请勿告知陌生人账号密码，并排查手机是否被植入木马导致密码泄露,点击确定重新登录");

        setViewClickListener(this, tvSubmit);
        /**全屏popup*/

        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(false);  //点击屏幕外是否dismiss
        setBackPressEnable(false);  //返回键是否可以取消掉PopupWindow
        setCanBack(false);//返回键是否可以取消掉PopupWindow
    }

    @Override
    protected Animation initShowAnimation() {
        return null;
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
    public Animator initExitAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mAnimaView, "translationY", 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OverShootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mAnimaView, "alpha", 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popup_fullscreen);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    public interface CallBack {
        void submit();
    }
}
