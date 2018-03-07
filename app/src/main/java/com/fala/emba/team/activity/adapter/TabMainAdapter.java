package com.fala.emba.team.activity.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fala.emba.team.R;
import com.fala.emba.team.view.textview.SpacingTextView;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;

import java.util.List;


/**
 *
 * @author SoMustYY
 * @date 2017/12/3
 *ViewPager的适配器
 */
public class TabMainAdapter extends IndicatorFragmentPagerAdapter {
    private String[] tabNames = {"协会", "活动", "我的"};
    private int[] tabIcons = {R.drawable.maintab_1_selector, R.drawable.maintab_2_selector, R.drawable.maintab_3_selector};
    private LayoutInflater inflater;
    private List<Fragment> mFragments;
    public TabMainAdapter(FragmentManager fragmentManager, Context context, List<Fragment> mFragments) {
        super(fragmentManager);
        inflater = LayoutInflater.from(context);
        this.mFragments = mFragments;
    }
    @Override
    public int getCount() {
        return mFragments.size();
    }
    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_main, container, false);
        }
        SpacingTextView spacingTextView = (SpacingTextView) convertView;
        spacingTextView.setText(tabNames[position]);
        spacingTextView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[position], 0, 0);
        spacingTextView.setLetterSpacing(10);
        return spacingTextView;
    }
    @Override
    public Fragment getFragmentForPage(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
        return mFragments.get(position);
    }
}
