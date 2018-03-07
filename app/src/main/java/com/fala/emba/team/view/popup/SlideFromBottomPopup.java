package com.fala.emba.team.view.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;


import com.fala.emba.team.R;
import com.fala.emba.team.view.popup.interpolator.OverShootInterpolator;

import razerdp.basepopup.BasePopupWindow;


/**
 * Created by 大灯泡 on 2016/1/15.
 * 从底部滑上来的popup
 */
public class SlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;

    public interface BottomPopupClick{
        void firstClick();
        void secondClick();
        void thirdClick();
    }
    public BottomPopupClick mBottomPopupClick;


    public SlideFromBottomPopup(Activity context, BottomPopupClick bottomPopupClick) {
        super(context);
        this.mBottomPopupClick = bottomPopupClick;
        bindEvent();
//        setDismissWhenTouchOuside(true);  //点击屏幕外是否dismiss
        setCanBack(true);
        setDismissWhenTouchOuside(true);  //点击屏幕外是否dismiss
        setBackPressEnable(true);  //返回键是否可以取消掉PopupWindow
        setFullScreen();//全屏问题
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateAnimation(0, 250 * 2, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_from_bottom, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.btn_1).setOnClickListener(this);
            popupView.findViewById(R.id.btn_2).setOnClickListener(this);
            popupView.findViewById(R.id.btn_3).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                mBottomPopupClick.firstClick();
                break;
            case R.id.btn_2:
                mBottomPopupClick.secondClick();
                break;
            case R.id.btn_3:
                mBottomPopupClick.thirdClick();
                break;
            default:
                break;
        }

    }
}
