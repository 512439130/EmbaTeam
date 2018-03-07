package com.fala.emba.team.util.time;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import com.fala.emba.team.R;

/**
 * Created by Administrator on 2017/12/1.
 * 倒计时工具类
 */

public class CountDownTimerUtils extends CountDownTimer {
    private Button mButton; //显示倒计时的文字

    /**
     * @param button          The Button
     * @param millisInFuture    millisInFuture  从开始调用start()到倒计时完成
     *                          并onFinish()方法被调用的毫秒数。（译者注：倒计时时间，单位毫秒）
     * @param countDownInterval 接收onTick(long)回调的间隔时间。（译者注：单位毫秒）
     */
    public CountDownTimerUtils(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mButton = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setClickable(false); //设置不可点击
        mButton.setText(millisUntilFinished / 1000 + "秒后可重新发送");  //设置倒计时时间
        mButton.setEnabled(false); //设置按钮为灰色，这时是不能点击的
        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mButton.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mButton.setText(spannableString);

    }

    @Override
    public void onFinish() {
        mButton.setText("重新获取验证码");
        mButton.setClickable(true);//重新获得点击
        mButton.setEnabled(true);//还原背景色
    }
}


