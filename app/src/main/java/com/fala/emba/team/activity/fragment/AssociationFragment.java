package com.fala.emba.team.activity.fragment;


import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.activity.DetailsWebActivity;
import com.fala.emba.team.activity.adapter.AssociationListAdapter;
import com.fala.emba.team.base.usage.BaseLazyFragment;
import com.fala.emba.team.bean.AssociationComm;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.constant.RequestConst;
import com.fala.emba.team.util.KeybordUtil;
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

import cn.sharesdk.util.ShareUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/3.
 * 主页
 */
public class AssociationFragment extends BaseLazyFragment {
    private static final String TAG = "AssociationFragment";
    private RefreshLayout mRefreshLayout;
    private ListView listViewAssociation;
    private AssociationListAdapter associationListAdapter;
    private UserComm.UserBean userBean;
    private List<AssociationComm.AssociationsBean> associationsBeans;
    private int pageOffset = 1;
    private EditText mSearchView;
    private TextView mSearchViewHint;
    private LinearLayout llLayoutNull;
    private TextView textNull;
    private MaterialHeader materialHeader;

    @Override
    protected String getTitleText() {
        return "协会";
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_association;
    }
    @Override
    protected boolean isShowBacking() {
        return false;
    }
    @Override
    protected boolean isShowTvRight() {
        return false;
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
        mSearchView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    mSearchViewHint.setVisibility(View.GONE);
                    KeybordUtil.openKeybord(mSearchView, mActivity);
                    mSearchView.setCursorVisible(true);// 再次点击显示光标
                }
                return false;
            }
        });
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //以下方法防止两次发送请求
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            LogManager.d(TAG,"搜索内容是"+v.getText().toString().trim());
                            String searchText = v.getText().toString().trim();
                            associationsBeans.clear();
                            pageOffset = 1;
                            doAssociationRequest(searchText);
                            mSearchViewHint.setVisibility(View.VISIBLE);
                            KeybordUtil.closeKeybord(mSearchView, mActivity);
                            mSearchView.setText("");
                            mSearchView.setCursorVisible(false);
                            //输入框失去焦点
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        listViewAssociation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogManager.d(TAG, "你点击了：" + position);
                DetailsWebActivity.openWebViewActivity(mActivity,associationsBeans.get(position).getIntroduce(),String.valueOf(associationsBeans.get(position).getId()),"",userBean.getPhone(),"协会详情");
            }
        });

        //分享
        tvTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtil.doShare(mActivity);
            }
        });
    }
    private void getShareData() {
        String data = SharePrefUtil.getString(mActivity, Consts.USER_INFO, null);
        userBean = new Gson().fromJson(data, UserComm.UserBean.class);
    }
    private void initView() {
        mRefreshLayout = findView(R.id.refresh_layout);
        listViewAssociation = findView(R.id.association_list_view);
        mSearchView = findView(R.id.association_search_view);
        mSearchViewHint = findView(R.id.search_hing_view);
        associationsBeans = new ArrayList<>();
        materialHeader = (MaterialHeader) mRefreshLayout.getRefreshHeader();
        ClassicsFooter classicsFooter = (ClassicsFooter) mRefreshLayout.getRefreshFooter();
        mRefreshLayout.setRefreshHeader(materialHeader);
        mRefreshLayout.setRefreshFooter(classicsFooter);
        llLayoutNull = findView(R.id.ll_layout_null);
        textNull = findView(R.id.text_null);

        tvTitleRight = findView(R.id.tv_title_right);
    }
    private void initRefreshOrLoadMore() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                associationsBeans.clear();
                pageOffset = 1;
                doAssociationRequest( "");
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageOffset++;
                doAssociationRequest( "");
            }
        });
        materialHeader.setShowBezierWave(true); //打开背景:
        mRefreshLayout.setEnableHeaderTranslationContent(true);//内容跟随偏移
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(false);  //内容不满1页时，不可加载更多
        mRefreshLayout.setEnableAutoLoadmore(false);  //关闭自动加载更多
        mRefreshLayout.setDisableContentWhenRefresh(true);  //设置是否开启在刷新时候禁止操作内容视图
    }
    private void doAssociationRequest(String searchText) {
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.resetNoMoreData();
        Map<String, String> params = new HashMap<>();
        params.put("phone", userBean.getPhone());  //手机号
        params.put("type", "0");  //全部类型
        params.put("page", String.valueOf(pageOffset));
        params.put("size", "5");  //每页6条
        params.put("searchStr",searchText);  //搜索内容
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
                                    listViewAssociation.setVisibility(View.VISIBLE);
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
                                        listViewAssociation.setVisibility(View.GONE);
                                        textNull.setText("亲，此协会不存在~");
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
        listViewAssociation.setAdapter(associationListAdapter);
        tvTitle.setOnClickListener(new View.OnClickListener() {  //成功加载第一次数据后，添加头部监听事件
            @Override
            public void onClick(View v) {
                listViewAssociation.setSelectionAfterHeaderView();//回顶部
                pageOffset = 1;
                mRefreshLayout.autoRefresh();  //初次加载自动刷新
            }
        });
    }
}
