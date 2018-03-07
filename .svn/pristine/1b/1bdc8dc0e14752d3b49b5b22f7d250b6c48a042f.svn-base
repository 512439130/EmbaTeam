package com.fala.emba.team.activity.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fala.emba.team.R;
import com.fala.emba.team.activity.DetailsWebActivity;
import com.fala.emba.team.activity.adapter.ActivityListAdapter;
import com.fala.emba.team.base.usage.BaseLazyFragment;
import com.fala.emba.team.bean.ActivityComm;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.constant.RequestConst;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.util.ToastManager;
import com.google.gson.Gson;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/6.
 * 已完成的活动
 */

public class CompleteActivityFragment extends BaseLazyFragment {
    private static final String TAG = "CompleteActivityFragment";
    private RefreshLayout mRefreshLayout;
    private ListView listViewActivity;
    private ActivityListAdapter activityListAdapter;
    private UserComm.UserBean userBean;
    private List<ActivityComm.ActivitiesBean> activitiesBeans;
    private int pageOffset = 1;
    private LinearLayout llLayoutNull;
    private MaterialHeader materialHeader;

    @Override
    protected String getTitleText() {
        return null;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete_activity;
    }
    protected void inits() {
        getShareData();
        initView();
        initRefreshOrLoadMore();
        initListener();
        mRefreshLayout.autoRefresh();  //初次加载自动刷新
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        listViewActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogManager.d(TAG, "你点击了：" + position);
                DetailsWebActivity.openWebViewActivity(mActivity,activitiesBeans.get(position).getDetailUrl(),"",String.valueOf(activitiesBeans.get(position).getId()),userBean.getPhone(),"活动详情");
            }
        });
    }
    private void getShareData() {
        String data = SharePrefUtil.getString(mActivity, Consts.USER_INFO, null);
        userBean = new Gson().fromJson(data, UserComm.UserBean.class);
    }
    private void initView() {
        mRefreshLayout = findView(R.id.refresh_layout);
        listViewActivity = findView(R.id.association_list_view);
        activitiesBeans = new ArrayList<>();
        materialHeader = (MaterialHeader) mRefreshLayout.getRefreshHeader();
        ClassicsFooter classicsFooter = (ClassicsFooter) mRefreshLayout.getRefreshFooter();
        mRefreshLayout.setRefreshHeader(materialHeader);
        mRefreshLayout.setRefreshFooter(classicsFooter);
        llLayoutNull = findView(R.id.ll_layout_null);
    }
    private void initRefreshOrLoadMore() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                activitiesBeans.clear();
                pageOffset = 1;
                doAssociationRequest();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageOffset++;
                doAssociationRequest();
            }
        });
        materialHeader.setShowBezierWave(true); //打开背景:
        mRefreshLayout.setEnableHeaderTranslationContent(true);//内容跟随偏移
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(false);  //内容不满1页时，不可加载更多
        mRefreshLayout.setEnableAutoLoadmore(false);  //关闭自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);  //设置是否开启在刷新时候禁止操作内容视图
    }
    private void doAssociationRequest() {
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.resetNoMoreData();
        Map<String, String> params = new HashMap<>();
        params.put("phone", userBean.getPhone());  //手机号
        params.put("type", "2");  //已完成
        params.put("page", String.valueOf(pageOffset));
        params.put("size", "5");  //每页6条
        params.put("searchStr", "");  //搜索内容
        OkHttpUtils
                .post()
                .url(RequestConst.APP_GET_ACTIVITY_LIST)
                .params(params)
                .addToken(SharePrefUtil.getString(mActivity, Consts.TOKEN, null))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogManager.e(TAG, e.getMessage());
                        mRefreshLayout.finishRefresh();
                        mRefreshLayout.finishLoadmore();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        LogManager.d(TAG, response);
                        ActivityComm activityComm = new Gson().fromJson(response, ActivityComm.class);
                        if (activityComm != null) {
                            if (activityComm.getStatusCode() == 1) {
                                if (activityComm.getActivities().size() != 0) {
                                    llLayoutNull.setVisibility(View.GONE);
                                    listViewActivity.setVisibility(View.VISIBLE);
                                    if (activitiesBeans.size() == 0) {
                                        mRefreshLayout.finishRefresh(100);
                                        activitiesBeans = activityComm.getActivities();
                                        initAdapterData();
                                    } else {
                                        mRefreshLayout.finishLoadmore(100);
                                        activitiesBeans.addAll(activityComm.getActivities());
                                        activityListAdapter.notifyDataSetChanged();//更新
                                    }
                                } else {
                                    mRefreshLayout.finishRefresh();
                                    mRefreshLayout.finishLoadmore();
                                    if(pageOffset == 1){
                                        llLayoutNull.setVisibility(View.VISIBLE);
                                        listViewActivity.setVisibility(View.GONE);
                                    }
                                    mRefreshLayout.finishLoadmoreWithNoMoreData();
                                    mRefreshLayout.setEnableLoadmore(false);
                                }
                            } else {
                                switch (activityComm.getStatusCode()) {
                                    case -301://系统异常，请联系管理员
                                        ToastManager.showShortText(mActivity, "系统异常，请联系管理员");
                                        break;
                                    case -302://校验失败，token无效
                                        SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                        mActivity.mLoginOvertimePopup.showPopupWindow();
                                        break;
                                    case -303://校验失败，token为空
                                        SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                        mActivity.mLoginOvertimePopup.showPopupWindow();
                                        break;
                                    default:
                                        ToastManager.showShortText(mActivity, activityComm.getMessage());
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    private void initAdapterData() {
        activityListAdapter = new ActivityListAdapter(mActivity, activitiesBeans);
        listViewActivity.setAdapter(activityListAdapter);
    }
}
