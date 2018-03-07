package com.fala.emba.team.base.usage;


import android.os.Bundle;


/**
 * Fragment嵌套WebView懒加载基类
 * Created by SoMustYY on 2017/7/22.
 */
public abstract class BaseWebViewFragment2 extends BaseWebLazyFragment {
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
//        getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏ActionBar
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
        setContentView(getLayoutId());  //填充布局
        inits();
    }

    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化
     */
    protected abstract void inits();
}
