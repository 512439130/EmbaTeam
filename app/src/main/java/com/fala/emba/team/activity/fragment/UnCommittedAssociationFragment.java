package com.fala.emba.team.activity.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fala.emba.team.R;
import com.fala.emba.team.activity.DetailsWebActivity;
import com.fala.emba.team.activity.adapter.AssociationListAdapter;
import com.fala.emba.team.base.usage.BaseLazyFragment;
import com.fala.emba.team.bean.AssociationComm;
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
 * 未参加的协会
 */

public class UnCommittedAssociationFragment extends BaseLazyFragment {
    private static final String TAG = "UnCommittedAssociationFragment";
    private RefreshLayout mRefreshLayout;
    private ListView listViewAssciation;
    private AssociationListAdapter associationListAdapter;
    private UserComm.UserBean userBean;
    private List<AssociationComm.AssociationsBean> associationsBeans;
    private int pageOffset = 1;
    private LinearLayout llLayoutNull;
    private MaterialHeader mMaterialHeader;
    private ClassicsFooter classicsFooter;
    @Override
    protected String getTitleText() {
        return null;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_un_committed_association;
    }
    @Override
    protected void inits() {
        getShareData();
        initView();
        initRefreshOrLoadMore();
        initListener();
        mRefreshLayout.autoRefresh();  //初次加载自动刷新
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        listViewAssciation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogManager.d(TAG, "你点击了：" + position);
                DetailsWebActivity.openWebViewActivity(mActivity,associationsBeans.get(position).getIntroduce(),String.valueOf(associationsBeans.get(position).getId()),"",userBean.getPhone(),"协会详情");            }
        });
    }
    private void getShareData() {
        String data = SharePrefUtil.getString(mActivity, Consts.USER_INFO, null);
        userBean = new Gson().fromJson(data, UserComm.UserBean.class);
    }
    private void initView() {
        mRefreshLayout = findView(R.id.refresh_layout);
        listViewAssciation = findView(R.id.association_list_view);
        associationsBeans = new ArrayList<>();
        mMaterialHeader = (MaterialHeader) mRefreshLayout.getRefreshHeader();
        classicsFooter = (ClassicsFooter) mRefreshLayout.getRefreshFooter();
        mRefreshLayout.setRefreshHeader(mMaterialHeader);
        mRefreshLayout.setRefreshFooter(classicsFooter);
        llLayoutNull = findView(R.id.ll_layout_null);
    }
    private void initRefreshOrLoadMore() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                associationsBeans.clear();
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
        mMaterialHeader.setShowBezierWave(true); //打开背景:
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
        params.put("type", "1");  //全部类型
        params.put("page", String.valueOf(pageOffset));
        params.put("size", "5");  //每页6条
        params.put("searchStr", "");  //搜索内容
        OkHttpUtils
                .post()
                .url(RequestConst.APP_GET_ASSOCIATION_LIST)
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
                        AssociationComm associationComm = new Gson().fromJson(response, AssociationComm.class);
                        if (associationComm != null) {
                            if (associationComm.getStatusCode() == 1) {
                                if (associationComm.getAssociations().size() != 0) {
                                    llLayoutNull.setVisibility(View.GONE);
                                    listViewAssciation.setVisibility(View.VISIBLE);
                                    if (associationsBeans.size() == 0) {
                                        mRefreshLayout.finishRefresh(100);
                                        associationsBeans = associationComm.getAssociations();
                                        initAdapterData();
                                    } else {
                                        mRefreshLayout.finishLoadmore(100);
                                        associationsBeans.addAll(associationComm.getAssociations());
                                        associationListAdapter.notifyDataSetChanged();//更新
                                    }
                                } else {
                                    mRefreshLayout.finishRefresh();
                                    mRefreshLayout.finishLoadmore();
                                    if(pageOffset == 1){
                                        llLayoutNull.setVisibility(View.VISIBLE);
                                        listViewAssciation.setVisibility(View.GONE);
                                    }
                                    mRefreshLayout.finishLoadmoreWithNoMoreData();
                                    mRefreshLayout.setEnableLoadmore(false);
                                }
                            } else {
                                switch (associationComm.getStatusCode()) {
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
                                        ToastManager.showShortText(mActivity, associationComm.getMessage());
                                        break;
                                }
                            }
                        }
                    }
                });
    }
    private void initAdapterData() {
        associationListAdapter = new AssociationListAdapter(mActivity, associationsBeans);
        listViewAssciation.setAdapter(associationListAdapter);
    }
}
