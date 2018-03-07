package com.fala.emba.team.util;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;



/**
 * Created by SoMustYY on 2017/9/5.
 * 图片点击动画
 */

public class AnimationUtil {
    /**
     * 图片点击效果（变小）
     *
     * @return
     */
    public static AnimationSet ImageViewAnimationBigOrSmall() {
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sAnimation.setFillAfter(true);
        AlphaAnimation alAnimation = new AlphaAnimation(1.0f, 0f);
        alAnimation.setFillAfter(true);

        set.addAnimation(sAnimation);
        set.addAnimation(alAnimation);

        set.setDuration(200);
        return set;
    }

    /**
     * 图片点击效果（变大）
     *
     * @return
     */
    public static AnimationSet ImageViewAnimationSmallOrBig() {
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation sAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sAnimation.setFillAfter(true);
        AlphaAnimation alAnimation = new AlphaAnimation(1.0f, 0f);
        alAnimation.setFillAfter(true);

        set.addAnimation(sAnimation);
        set.addAnimation(alAnimation);

        set.setDuration(200);
        return set;
    }
}
