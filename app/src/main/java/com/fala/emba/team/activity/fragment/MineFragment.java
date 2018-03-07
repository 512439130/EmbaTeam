package com.fala.emba.team.activity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.activity.AboutActivity;
import com.fala.emba.team.activity.ActivityMineTabActivity;
import com.fala.emba.team.activity.AssociationMineTabActivity;
import com.fala.emba.team.activity.MineSettingActivity;
import com.fala.emba.team.activity.PushHistoryListActivity;
import com.fala.emba.team.app.AppManager;
import com.fala.emba.team.base.usage.BaseNoLazyFragment;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.util.ImageLoaderUtils;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.view.popup.DialogPopup;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Administrator
 * @date 2017/12/3
 * 我的TAB
 */
public class MineFragment extends BaseNoLazyFragment {
    private static final String TAG = "MineFragment";
    private UserComm.UserBean userBean;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userPhone;
    private ImageView userSetting;
    private Button btnLogout;
    private RelativeLayout rlMineAssociation;
    private RelativeLayout rlMineActivity;
    private RelativeLayout rlMinePush;
    private RelativeLayout rlMineAbout;
    private DialogPopup mLogoutDialogPopup;
    @SuppressLint("HandlerLeak")
    private Handler popupHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    initLogoutDialog();
                    break;
                case 1:
                    doLogout();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected String getTitleText() {
        return "测试Tile";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
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
        initView();
        getData();
        initListener();
        popupHandler.sendEmptyMessageDelayed(0, 1000);
    }

    private void initLogoutDialog() {
        mLogoutDialogPopup = new DialogPopup(mActivity, new DialogPopup.DialogCallBack() {
            @Override
            public void submit() {
                mLogoutDialogPopup.dismiss();
                popupHandler.sendEmptyMessageDelayed(1, 1000);
            }

            @Override
            public void cancel() {
                mLogoutDialogPopup.dismiss();
            }
        });
        mLogoutDialogPopup.setSubmitText("退出");
        mLogoutDialogPopup.setContentText("亲，是否退出？");
    }

    private void initView() {
        userImage = findView(R.id.user_image);
        userName = findView(R.id.user_name);
        userPhone = findView(R.id.user_phone);
        userSetting = findView(R.id.user_setting);
        btnLogout = findView(R.id.btn_logout);
        rlMineAssociation = findView(R.id.rl_mine_association);
        rlMineActivity = findView(R.id.rl_mine_activity);
        rlMinePush = findView(R.id.rl_mine_push);
        rlMineAbout = findView(R.id.rl_mine_about);
    }

    public void getData() {
        String data = SharePrefUtil.getString(mActivity, Consts.USER_INFO, null);
        if (data != null) {
            userBean = new Gson().fromJson(data, UserComm.UserBean.class);
            initData();
        } else {
            LogManager.e(TAG, "用户信息为空");
        }
    }

    private void initData() {
        ImageLoaderUtils.displayImageDefault1(userBean.getHeadUrl(), userImage);
        userName.setText(userBean.getName());
        userPhone.setText(userBean.getPhone());
    }

    private void initListener() {
        userSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageViewAnimation(userSetting);
                startActivity(new Intent(mActivity, MineSettingActivity.class));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLogoutDialogPopup.showPopupWindow();
            }
        });
        rlMineAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AssociationMineTabActivity.class));
            }
        });
        rlMineActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, ActivityMineTabActivity.class));
            }
        });
        rlMinePush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, PushHistoryListActivity.class));
            }
        });
        rlMineAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, AboutActivity.class));
            }
        });
    }

    /**
     * 退出
     */
    private void doLogout() {
        SharePrefUtil.saveString(mActivity, Consts.TOKEN, null);
        SharePrefUtil.saveString(mActivity, Consts.USER_INFO, null);
        SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
        SharePrefUtil.saveString(mActivity, Consts.USER_PHONE, userBean.getPhone());
        AppManager.getAppManager().AppExit(mActivity);
        AppManager.getAppManager().finishAllActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogManager.e(TAG, "onCreate");
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        LogManager.e(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogManager.e(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogManager.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogManager.e(TAG, "onResume");
        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogManager.e(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogManager.e(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogManager.e(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogManager.e(TAG, "onDestroy");
        popupHandler.removeMessages(0);
        popupHandler.removeMessages(1);
    }
}
