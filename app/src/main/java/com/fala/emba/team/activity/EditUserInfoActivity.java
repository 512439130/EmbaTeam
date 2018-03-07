package com.fala.emba.team.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.fala.emba.team.R;
import com.fala.emba.team.activity.adapter.EditExpandableListViewAdapter;
import com.fala.emba.team.base.activity.BaseActivity;
import com.fala.emba.team.bean.UpdateUserInfo;
import com.fala.emba.team.bean.UploadComm;
import com.fala.emba.team.bean.UserComm;
import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.constant.RequestConst;
import com.fala.emba.team.util.DateUtil;
import com.fala.emba.team.util.ImageLoaderUtils;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.util.ToastManager;
import com.fala.emba.team.util.photo.ActivityPhotoUtils;
import com.fala.emba.team.view.CustomExpandableListView;
import com.fala.emba.team.view.popup.SlideFromBottomPopup;
import com.google.gson.Gson;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/4.
 */

public class EditUserInfoActivity extends BaseActivity {
    private static final String TAG = "EditUserInfoActivity";
    private static final int REQUEST_CODE_PERMISSION_TAKE_PICTURE = 100;// 拍照
    private static final int REQUEST_CODE_PERMISSION_SELECT_PICTURE = 200; //本地选择图片
    private static final int REQUEST_CODE_SETTING = 300;
    private String[] perms = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    UserComm.UserBean userBean;
    private CircleImageView userImage;
    private TextView userName;
    private TextView userPhone;
    private TextView userSex;
    private TextView userClass;
    private TextView userIdentity;
    private EditText userCompany;
    private EditText userJob;
    private TextView userHeight;
    private TextView userBirthday;
    private EditText userClothesSize;
    private Button btnSaveUserInfo;
    private TextView userNameTitle;
    private TextView userSexTitle;
    private TextView userIdentityTitle;
    private TextView userCompanyTitle;
    private TextView userJobTitle;
    private TextView userHeightTitle;
    private TextView userBirthdayTitle;
    private TextView userClothesSizeTitle;
    private RelativeLayout rlLayoutImage;
    private RelativeLayout rlLayoutPhone;
    private RelativeLayout rlLayoutSex;
    private RelativeLayout rlLayoutIdentity;
    private RelativeLayout rlLayoutHeight;
    private RelativeLayout rlLayoutBirthday;
    private OptionsPickerView sexTimePickerView;
    private OptionsPickerView identityPickerView;
    private OptionsPickerView heightPickerView;
    private TimePickerView birthDayPickerView;
    private List<String> sexes;
    private List<String> identities;
    private List<String> heights;
    private String heightText;
    private SlideFromBottomPopup slideFromBottomPopup;
    private ActivityPhotoUtils activityPhotoUtils;
    private Uri photoUri;
    private String time;  //上传时间
    private String phone;  //上传者手机号
    private CustomExpandableListView expandableListView;
    private List<UserComm.UserBean.AssociationsBean> associationsBeans;
    @Override
    protected String getTitleText() {
        return "编辑个人信息";
    }
    @Override
    protected boolean isShowStatus() {
        return true;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_user_info;
    }
    @Override
    protected boolean isShowBacking() {
        return true;
    }
    @Override
    protected boolean isShowTvRight() {
        return false;
    }
    @Override
    protected void inits() {
        initView();
        getLocalData();
        getData();
        initListener();
        setPortraitChangeListener();//头像相关回调
    }
    private void initExpandableListView() {
        associationsBeans = userBean.getAssociations();
        if (associationsBeans != null) {
            if (associationsBeans.size() != 0) {
                EditExpandableListViewAdapter editExpandableListViewAdapter = new EditExpandableListViewAdapter(mActivity, associationsBeans);
                expandableListView.setAdapter(editExpandableListViewAdapter);
                editExpandableListViewAdapter.notifyDataSetChanged();
            }
        }
    }


    private void getData() {
        String data = SharePrefUtil.getString(mActivity, Consts.USER_INFO, null);
        if (data != null) {
            userBean = new Gson().fromJson(data, UserComm.UserBean.class);
            phone = userBean.getPhone();
            initData();
            initPickerView();//自定义文字选择器
            initTimePicker();//时间选择器
        } else {
            LogManager.e(TAG, "用户信息为空");
        }
    }
    /**
     * 初始化自定义选择器
     */
    private void initPickerView() {
        sexTimePickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                LogManager.d(TAG, sexes.get(options1) + "");
                userSex.setText(sexes.get(options1) + "");
            }
        }).setLayoutRes(R.layout.picker_view_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView tvCancel = v.findViewById(R.id.tv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sexTimePickerView.returnData();
                        sexTimePickerView.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sexTimePickerView.dismiss();
                    }
                });
            }
        }).isDialog(true).build();
        sexTimePickerView.setPicker(sexes);//添加数据
        heightPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                LogManager.d(TAG, heights.get(options1) + "");
                userHeight.setText(heights.get(options1));
            }
        }).setLayoutRes(R.layout.picker_view_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView ivCancel = v.findViewById(R.id.tv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        heightPickerView.returnData();
                        heightPickerView.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        heightPickerView.dismiss();
                    }
                });
            }
        }).isDialog(true).build();
        heightPickerView.setPicker(heights);//添加数据
        identityPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                LogManager.d(TAG, identities.get(options1) + "");
                userIdentity.setText(identities.get(options1) + "");
            }
        }).setLayoutRes(R.layout.picker_view_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                TextView ivCancel = v.findViewById(R.id.tv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        identityPickerView.returnData();
                        identityPickerView.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        identityPickerView.dismiss();
                    }
                });

            }
        }).isDialog(true).build();
        identityPickerView.setPicker(identities);//添加数据
    }
    /**
     * 初始化时间选择器（年月日）
     */
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        birthDayPickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                userBirthday.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setTitleText("请选择")
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    private String getTime(Date date) {//可根据需要自行截取数据显示
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 模板数据初始化
     */
    private void getLocalData() {
        sexes = new ArrayList<>();
        sexes.add(0, "男");
        sexes.add(1, "女");
        sexes.add(2, "  ");
        identities = new ArrayList<>();
        identities.add(0, "EMBA");
        identities.add(1, "MBA");
        identities.add(2, "EDP");
        identities.add(3, "华工校友");
        identities.add(4, "其他");
        identities.add(5, "  ");
        heights = new ArrayList<>();
        for (int i = 100, j = 0; i <= 251; i++, j++) {
            heights.add(j, i + "");
        }
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        ImageLoaderUtils.displayImageDefault1(userBean.getHeadUrl(), userImage);
        userName.setText(userBean.getName());
        userPhone.setText(userBean.getPhone());
        userSex.setText(userBean.getSex());
        userClass.setText(userBean.getClassName());
        userIdentity.setText(userBean.getIdentity());
        userCompany.setText(userBean.getCompany());
        userJob.setText(userBean.getJob());
        userHeight.setText(userBean.getHeight());
        userBirthday.setText(userBean.getBirthday());
        userClothesSize.setText(userBean.getClothesSize());
        if(userBean.getName().equals("")){
            userNameTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getSex().equals("")){
            userSexTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getIdentity().equals("")){
            userIdentityTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getCompany().equals("")){
            userCompanyTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getJob().equals("")){
            userJobTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getHeight().equals("")){
            userHeightTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getBirthday().equals("")){
            userBirthdayTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
        if(userBean.getClothesSize().equals("")){
            userClothesSizeTitle.setTextColor(Color.parseColor("#00b4ff"));
        }
    }

    private void initView() {
        userImage = findView(R.id.edit_user_info_image);
        userName = findView(R.id.edit_user_info_name);
        userPhone = findView(R.id.edit_user_info_phone);
        userSex = findView(R.id.edit_user_info_sex);
        userClass = findView(R.id.edit_user_info_class);
        userIdentity = findView(R.id.edit_user_info_identity);
        userCompany = findView(R.id.edit_user_info_company);
        userJob = findView(R.id.edit_user_info_job);
        userHeight = findView(R.id.edit_user_info_height);
        userBirthday = findView(R.id.edit_user_info_birthday);
        userClothesSize = findView(R.id.edit_user_info_clothes_size);
        btnSaveUserInfo = findView(R.id.btn_save_user_info);
        rlLayoutImage = findView(R.id.rl_layout_image);
        rlLayoutPhone = findView(R.id.rl_layout_phone);
        rlLayoutSex = findView(R.id.rl_layout_sex);
        rlLayoutIdentity = findView(R.id.rl_layout_identity);
        rlLayoutHeight = findView(R.id.rl_layout_height);
        rlLayoutBirthday = findView(R.id.rl_layout_birthday);
        expandableListView = findView(R.id.edit_expandable_list_view);
        userNameTitle = findView(R.id.edit_user_info_name_title);
        userSexTitle = findView(R.id.edit_user_info_sex_title);
        userIdentityTitle = findView(R.id.edit_user_info_identity_title);
        userCompanyTitle = findView(R.id.edit_user_info_company_title);
        userJobTitle = findView(R.id.edit_user_info_job_title);
        userHeightTitle = findView(R.id.edit_user_info_height_title);
        userBirthdayTitle = findView(R.id.edit_user_info_birthday_title);
        userClothesSizeTitle = findView(R.id.edit_user_info_clothes_size_title);
    }
    private void initListener() {
        rlLayoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = String.valueOf(new Date().getTime());
                slideFromBottomPopup.showPopupWindow();
            }
        });
        rlLayoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UpdatePhoneActivity.class);
            }
        });
        rlLayoutSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < sexes.size(); i++) {
                    if (userSex.getText().toString().trim().equals(sexes.get(i))) {
                        sexTimePickerView.setSelectOptions(i);
                    }
                }
                sexTimePickerView.show();
            }
        });
        rlLayoutIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < identities.size(); i++) {
                    if (userIdentity.getText().toString().trim().equals(identities.get(i))) {
                        identityPickerView.setSelectOptions(i);
                    }
                }
                identityPickerView.show();
            }
        });
       heightText = userHeight.getText().toString().trim();
        rlLayoutHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < heights.size(); i++) {
                    if (heightText.equals(heights.get(i))) {
                        heightPickerView.setSelectOptions(i);
                    }
                }
                heightPickerView.show();
            }
        });
        rlLayoutBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateString = userBirthday.getText().toString().trim();
                birthDayPickerView.setDate(DateUtil.calendarToDate(dateString));
                birthDayPickerView.show();
            }
        });
        btnSaveUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditUserInfo();
            }
        });
        slideFromBottomPopup = new SlideFromBottomPopup(mActivity, new SlideFromBottomPopup.BottomPopupClick() {
            @Override
            public void firstClick() {
                slideFromBottomPopup.dismiss();
                takePicture();
            }

            @Override
            public void secondClick() {
                slideFromBottomPopup.dismiss();
                selectPicture();
            }
            @Override
            public void thirdClick() {
                slideFromBottomPopup.dismiss();
            }
        });
    }
    private void selectPicture() {
        AndPermission.with(mActivity)
                .requestCode(REQUEST_CODE_PERMISSION_SELECT_PICTURE)
                .permission(perms)
                .callback(permissionListener)
                .rationale(rationaleListener)
                .start();
    }
    private void takePicture() {
        AndPermission.with(mActivity)
                .requestCode(REQUEST_CODE_PERMISSION_TAKE_PICTURE)
                .permission(perms)
                .callback(permissionListener)
                .rationale(rationaleListener)
                .start();
    }
    /**
     * 拍照或本地选择图片成功后，生成图片在sd卡中，并得到图片位置
     */
    private void setPortraitChangeListener() {
        activityPhotoUtils = new ActivityPhotoUtils(new ActivityPhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    photoUri = uri;
                    //根据图片位置发送上传文件的网络请求
                    final String path = uri.getPath();
                    LogManager.e(TAG, "切图图片位置：" + path);
                    uploadImageRequest(new File(path));
                }
            }
            @Override
            public void onPhotoCancel() {
                LogManager.d(TAG, "取消选择");
            }
        });
    }
    private void uploadImageRequest(File file) {
        mLoadingDialog.show();
        if (!file.exists()) {
            ToastManager.showShortText(mActivity, "文件不存在，请修改文件路径");
        } else {
            Map<String, String> params = new HashMap<>();
            params.put("phone", userPhone.getText().toString().trim());
            OkHttpUtils
                    .post()
                    .addFile("file", "head_image.png", file)
                    .url(RequestConst.APP_UPLOAD_IMAGE)
                    .params(params)
                    .addToken(SharePrefUtil.getString(mActivity, Consts.TOKEN, null))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogManager.e(TAG, e.getMessage());
                            mLoadingDialog.dismiss();
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            LogManager.e(TAG, response);
                            UploadComm uploadComm = new Gson().fromJson(response, UploadComm.class);
                            mLoadingDialog.dismiss();
                            if (uploadComm != null) {
                                if (uploadComm.getStatusCode() == 1) {
                                    ToastManager.showShortText(mActivity, "头像上传成功");
                                    ImageLoaderUtils.displayImageDefault1(uploadComm.getHeadUrl(), userImage);
                                    activityPhotoUtils.clearCropFile(photoUri);
                                    updateUserInfo();
                                } else {
                                    switch (uploadComm.getStatusCode()) {
                                        case -301://系统异常，请联系管理员
                                            ToastManager.showShortText(mActivity, "系统异常，请联系管理员");
                                            break;
                                        case -302://校验失败，token无效
                                            SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                            mLoginOvertimePopup.showPopupWindow();
                                            break;
                                        case -303://校验失败，token为空
                                            SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                            mLoginOvertimePopup.showPopupWindow();
                                            break;
                                        default:
                                            ToastManager.showShortText(mActivity, uploadComm.getMessage());
                                            break;
                                    }
                                }
                            }
                        }
                    });
        }
    }
    private void updateUserInfo() {
        mLoadingDialog.show();
        OkHttpUtils
                .post()
                .url(RequestConst.APP_GET_USER_INFO)
                .addParams("phone", userBean.getPhone())
                .addToken(SharePrefUtil.getString(mActivity, Consts.TOKEN, null))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogManager.e(TAG, e.getMessage());
                        mLoadingDialog.dismiss();
                        ToastManager.showShortText(mActivity, "系统错误，请联系管理员");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        LogManager.d(TAG, "token：" + response);
                        mLoadingDialog.dismiss();
                        //保存账号信息
                        UserComm userComm = new Gson().fromJson(response, UserComm.class);
                        if(userComm.getStatusCode() == 1){
                            UserComm.UserBean userBean = userComm.getUser();
                            String data = new Gson().toJson(userBean);
                            SharePrefUtil.saveString(mActivity, Consts.USER_INFO, data);
                        }else{
                            switch (userComm.getStatusCode()) {
                                case -301://系统异常，请联系管理员
                                    ToastManager.showShortText(mActivity, "系统异常，请联系管理员");
                                    break;
                                case -302://校验失败，token无效
                                    SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                    mLoginOvertimePopup.showPopupWindow();
                                    break;
                                case -303://校验失败，token为空
                                    SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                    mLoginOvertimePopup.showPopupWindow();
                                    break;
                                default:
                                    ToastManager.showShortText(mActivity, userComm.getMessage());
                                    break;
                            }
                        }
                    }
                });
    }

    private void doEditUserInfo() {
        mLoadingDialog.show();
        btnSaveUserInfo.setClickable(false);//设置button不可以点击
        Map<String, String> params = new HashMap<>();
        params.put("phone", userPhone.getText().toString().trim());
        params.put("name", userName.getText().toString().trim());
        params.put("sex", userSex.getText().toString().trim());
        params.put("identity", userIdentity.getText().toString().trim());
        params.put("company", userCompany.getText().toString().trim());
        params.put("job", userJob.getText().toString().trim());
        params.put("birthday", userBirthday.getText().toString().trim());
        params.put("clothesSize", userClothesSize.getText().toString().trim());
        params.put("height", userHeight.getText().toString().trim());
        OkHttpUtils
                .post()
                .url(RequestConst.APP_EDIT_USER)
                .params(params)
                .addToken(SharePrefUtil.getString(mActivity, Consts.TOKEN, null))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogManager.e(TAG, e.getMessage());
                        btnSaveUserInfo.setClickable(true);
                        mLoadingDialog.dismiss();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        LogManager.d(TAG, response);
                        //updateUserInfo
                        UpdateUserInfo updateUserInfo = new Gson().fromJson(response, UpdateUserInfo.class);
                        if (updateUserInfo.getStatusCode() == 1) {
                            ToastManager.showShortText(mActivity, "资料完善成功");
                            String data = new Gson().toJson(updateUserInfo.getUser());
                            SharePrefUtil.saveString(mActivity, Consts.USER_INFO, data);
                            finish();
                        } else {
                            btnSaveUserInfo.setClickable(true);
                            switch (updateUserInfo.getStatusCode()) {
                                case -301://系统异常，请联系管理员
                                    ToastManager.showShortText(mActivity, "系统异常，请联系管理员");
                                    break;
                                case -302://校验失败，token无效
                                    SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                    mLoginOvertimePopup.showPopupWindow();
                                    break;
                                case -303://校验失败，token为空
                                    SharePrefUtil.saveBoolean(mActivity, Consts.IS_LOGIN, false);
                                    mLoginOvertimePopup.showPopupWindow();
                                    break;
                                default:
                                    ToastManager.showShortText(mActivity, updateUserInfo.getMessage());
                                    break;
                            }

                        }
                        mLoadingDialog.dismiss();
                    }
                });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ActivityPhotoUtils.INTENT_CROP:
                activityPhotoUtils.onActivityResult(mActivity, requestCode, resultCode, data, phone, time, Consts.USER_IMAGE_NAME);
                break;
            case ActivityPhotoUtils.INTENT_TAKE:
                activityPhotoUtils.onActivityResult(mActivity, requestCode, resultCode, data, phone, time, Consts.USER_IMAGE_NAME);
                break;
            case ActivityPhotoUtils.INTENT_SELECT:
                activityPhotoUtils.onActivityResult(mActivity, requestCode, resultCode, data, phone, time, Consts.USER_IMAGE_NAME);
                break;
            case REQUEST_CODE_SETTING:
                ToastManager.showShortText(this, R.string.message_setting_back);
                break;
        }
    }
    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_TAKE_PICTURE:
                    activityPhotoUtils.takePicture(mActivity, phone, time, Consts.USER_IMAGE_NAME);
                    break;
                case REQUEST_CODE_PERMISSION_SELECT_PICTURE:
                    activityPhotoUtils.selectPicture(mActivity, phone, time, Consts.USER_IMAGE_NAME);
                    break;
            }
        }
        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_TAKE_PICTURE:
                    ToastManager.showShortText(mActivity, R.string.failure);
                    break;
                case REQUEST_CODE_PERMISSION_SELECT_PICTURE:
                    ToastManager.showShortText(mActivity, R.string.failure);
                    break;
            }
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(mActivity, deniedPermissions)) {
             AndPermission.defaultSettingDialog(mActivity, REQUEST_CODE_SETTING)
                     .setTitle("权限申请失败")
                     .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则系统功能无法正常使用！")
                     .setPositiveButton("好，去设置")
                     .show();
            }
        }
    };
    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            new AlertDialog.Builder(mActivity)
                    .setTitle(R.string.title_dialog)
                    .setMessage(R.string.message_permission_failed)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel();
                        }
                    }).show();
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        getData();
        initExpandableListView();
    }
}
