package com.fala.emba.team.base.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 一个让其支持App内部显示资源、支持JavaScript、支持显示进度条的WebView
 * Created by SoMustYY on 2017/7/22.
 */

public class BaseWebView extends WebView {
    private ProgressBar progressbar;

    WebSettings settings;

    public BaseWebView(Context context) {
        super(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProgressBar(context);
        inits();
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
    }

    private void initProgressBar(Context context) {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dp2px(context, 2), 0, 0));
        //改变progressbar默认进度条的颜色（绿色）为Color.GREEN
        progressbar.setProgressDrawable(new ClipDrawable(new ColorDrawable(Color.parseColor("#04d06f")), Gravity.LEFT, ClipDrawable.HORIZONTAL));
        addView(progressbar);
    }

    /**
     * 方法描述：启用支持javascript
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void inits() {
        settings = getSettings();
        settings.setJavaScriptEnabled(true);

//        // 设置可以支持缩放
//        settings.setSupportZoom(true);
//        // 设置出现缩放工具
//        settings.setBuiltInZoomControls(true);
//        //扩大比例的缩放
//        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存



        settings.setBlockNetworkImage(true);//本身含义阻止图片网络数据

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 方法描述：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 类描述：显示WebView加载的进度情况
     */
    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
                settings.setBlockNetworkImage(false);  //解除数据阻止

            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);

                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

}
