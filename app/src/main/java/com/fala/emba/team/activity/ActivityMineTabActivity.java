package com.fala.emba.team.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.fala.emba.team.R;
import com.fala.emba.team.activity.adapter.ActivityMineTabAdapter;
import com.fala.emba.team.activity.fragment.CompleteActivityFragment;
import com.fala.emba.team.activity.fragment.UnFinishedActivityFragment;
import com.fala.emba.team.animation.viewpager.DepthPageTransformer;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.util.DisplayUtils;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 * 我的活动
 */

public class ActivityMineTabActivity extends BaseActivity {
    private ViewPager viewPager;
    private Indicator indicator;
    @Override
    protected String getTitleText() {
        return "我的活动";
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
        return R.layout.activity_activity_mine_tab;
    }

    @Override
    protected void inits() {
        initView();
        initViewPagerIndicator();
    }
    private void initView() {
        viewPager = findView(R.id.activity_fragment_tab_main_viewPager);
        indicator = findView(R.id.activity_fragment_tab_main_indicator);
    }

    private void initViewPagerIndicator() {
        indicator.setOnTransitionListener(new OnTransitionTextListener(16, 15, Color.parseColor("#17BCFE"), Color.parseColor("#A7A7A7")));  //tab text_color and size
        indicator.setScrollBar(new ColorBar(getApplicationContext(), Color.parseColor("#17BCFE"), DisplayUtils.dp2px(this,2)));  //indicator_line
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator, viewPager);

        List<Fragment> mFragments = new ArrayList<>();
        UnFinishedActivityFragment unFinishedActivityFragment = new UnFinishedActivityFragment();
        CompleteActivityFragment completeActivityFragment = new CompleteActivityFragment();

        mFragments.add(unFinishedActivityFragment);
        mFragments.add(completeActivityFragment);

        indicatorViewPager.setAdapter(new ActivityMineTabAdapter(getSupportFragmentManager(),this,mFragments));
    }
}
