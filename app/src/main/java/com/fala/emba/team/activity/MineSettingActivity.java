package com.fala.emba.team.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.fala.emba.team.R;
import com.fala.emba.team.activity.adapter.MineExpandableListViewAdapter;
import com.fala.emba.team.app.GlideApp;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.util.ImageLoaderUtils;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.view.CustomExpandableListView;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/12/3.
 * 个人信息页
 */

public class MineSettingActivity extends BaseActivity {
    private static final String TAG = "MineSettingActivity";
    private UserComm.UserBean userBean;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userPhone;
    private ImageView userSex;
    //    private TextView userAssociation;
    private TextView userClass;
    private TextView userIdentity;
    private TextView userCompany;
    private TextView userJob;
    private TextView userHeight;
    private TextView userBirthday;
    private TextView userClothesSize;
    private Button btnEditUserInfo;
    private CustomExpandableListView expandableListView;
    private MineExpandableListViewAdapter expandableListAdapter;
    @Override
    protected boolean isShowBacking() {
        return true;
    }
    @Override
    protected boolean isShowTvRight() {
        return false;
    }
    @Override
    protected String getTitleText() {
        return "个人信息";
    }
    @Override
    protected boolean isShowStatus() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_setting;
    }
    @Override
    protected void inits() {
        initView();
        getData();
        initListener();
    }
    private void initExpandableListView() {
        List<UserComm.UserBean.AssociationsBean> associationsBeans = userBean.getAssociations();
        if(associationsBeans != null){
            if (associationsBeans.size() != 0) {
                expandableListAdapter = new MineExpandableListViewAdapter(mActivity, associationsBeans);
                expandableListView.setAdapter(expandableListAdapter);
            }
        }
    }
    private void initView() {
        userImage = findView(R.id.setting_user_image);
        userName = findView(R.id.setting_user_name);
        userPhone = findView(R.id.setting_user_phone);
        userSex = findView(R.id.setting_user_sex);

//        userAssociation = findView(R.id.setting_user_association);
        userClass = findView(R.id.setting_user_class);
        userIdentity = findView(R.id.setting_user_identity);
        userCompany = findView(R.id.setting_user_company);
        userJob = findView(R.id.setting_user_job);
        userHeight = findView(R.id.setting_user_height);
        userBirthday = findView(R.id.setting_user_birthday);
        userClothesSize = findView(R.id.setting_user_clothes_size);

        btnEditUserInfo = findView(R.id.btn_edit_user_info);

        expandableListView = findView(R.id.mine_expandable_list_view);
    }
    private void getData() {
        String data = SharePrefUtil.getString(mActivity, Consts.USER_INFO, null);
        if (data != null) {
            userBean = new Gson().fromJson(data, UserComm.UserBean.class);
            initData();

        } else {
            LogManager.e(TAG, "用户信息为空");
        }
    }
    @SuppressLint("SetTextI18n")
    private void initData() {
        ImageLoaderUtils.displayImageDefault1(userBean.getHeadUrl(), userImage);
        userName.setText(userBean.getName());
        userPhone.setText(userBean.getPhone());
        if (userBean.getSex().equals("")) {
            userSex.setVisibility(View.INVISIBLE);
        } else {
            if (userBean.getSex().equals("男")) {
                userSex.setImageResource(R.mipmap.icon_setting_man);
            } else if (userBean.getSex().equals("女")) {
                userSex.setImageResource(R.mipmap.icon_setting_women);
            }
        }
        userClass.setText(userBean.getClassName());
        userIdentity.setText(userBean.getIdentity());
        userCompany.setText(userBean.getCompany());
        userJob.setText(userBean.getJob());
        userHeight.setText(userBean.getHeight());
        userBirthday.setText(userBean.getBirthday());
        userClothesSize.setText(userBean.getClothesSize());
    }
    private void initListener() {
        btnEditUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EditUserInfoActivity.class);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
        initExpandableListView();
    }
}
