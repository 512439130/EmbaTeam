package com.fala.emba.team.util.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.util.List;


/**
 * [从本地选择图片以及拍照工具类，完美适配2.0-5.0版本]
 * Fragment使用
 **/
public class FragmentPhotoUtils {

    private final String TAG = FragmentPhotoUtils.class.getSimpleName();

    /**
     * 裁剪图片成功后返回
     **/
    public static final int INTENT_CROP = 2;
    /**
     * 拍照成功后返回
     **/
    public static final int INTENT_TAKE = 3;
    /**
     * 本地选择图片成功后返回
     **/
    public static final int INTENT_SELECT = 4;


    /**
     * PhotoUtils对象
     **/
    private OnPhotoResultListener onPhotoResultListener;


    public FragmentPhotoUtils(OnPhotoResultListener onPhotoResultListener) {
        this.onPhotoResultListener = onPhotoResultListener;
    }

    /**
     * 拍照
     *
     * @param
     * @return
     */
    public void takePicture(Fragment fragment, String userPhone, String time, String photoName) {
        Log.e(TAG,"点击dialogItem=拍照");
        try {
            //每次选择图片吧之前的图片删除
            Log.e(TAG,"开始清除之前的图片");
            clearCropFile(buildUri(fragment, userPhone, time, photoName));

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, buildUri(fragment, userPhone, time, photoName));
            if (!isIntentAvailable(fragment, intent)) {

                return;
            }
            fragment.startActivityForResult(intent, INTENT_TAKE);
            Log.e(TAG,"成功");
        } catch (Exception e) {
            Log.e(TAG,e.toString());
            e.printStackTrace();
        }
    }

    /***
     * 选择一张图片
     * 图片类型，这里是image/*，当然也可以设置限制
     * 如：image/jpeg等
     *
     * @param fragment Fragment
     */
    @SuppressLint("InlinedApi")
    public void selectPicture(Fragment fragment, String userPhone, String time, String photoName) {
        Log.e(TAG,"点击dialogItem=选择一张图片");
        try {
            //每次选择图片吧之前的图片删除
            clearCropFile(buildUri(fragment, userPhone, time, photoName));

            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            if (!isIntentAvailable(fragment, intent)) {
                return;
            }
            fragment.startActivityForResult(intent, INTENT_SELECT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建uri
     *
     * @param fragment
     * @return
     */
    private Uri buildUri(Fragment fragment, String userPhone, String time, String photoName) {
        if (CommonUtils.checkSDCard()) {
            Log.e(TAG,"CommonUtils.checkSDCard()");
            return Uri.fromFile(Environment.getExternalStorageDirectory()).buildUpon().appendPath(userPhone + "_" + time + "_" + photoName).build();
        } else {
            Log.e(TAG,"else");
            return Uri.fromFile(fragment.getActivity().getCacheDir()).buildUpon().appendPath(userPhone + "_" + time + "_" + photoName).build();
        }
    }

    /**
     * @param intent
     * @return
     */
    protected boolean isIntentAvailable(Fragment fragment, Intent intent) {
        PackageManager packageManager = fragment.getActivity().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private boolean corp(Fragment fragment, Uri uri, String userPhone, String time, String photoName) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 600);
        cropIntent.putExtra("outputY", 600);
        cropIntent.putExtra("return-data", false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        Uri cropuri = buildUri(fragment, userPhone, time, photoName);
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropuri);
        if (!isIntentAvailable(fragment, cropIntent)) {
            return false;
        } else {
            try {
                fragment.startActivityForResult(cropIntent, INTENT_CROP);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 返回结果处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Fragment fragment, int requestCode, int resultCode, Intent data, String userPhone, String time, String photoName) {
        if (onPhotoResultListener == null) {
            Log.e(TAG, "onPhotoResultListener is not null");
            return;
        }

        switch (requestCode) {
            //拍照
            case INTENT_TAKE:
                Log.e(TAG,"选择拍照");
                if (new File(buildUri(fragment, userPhone, time, photoName).getPath()).exists()) {
                    if (corp(fragment, buildUri(fragment, userPhone, time, photoName), userPhone, time, photoName)) {
                        return;
                    }
                    onPhotoResultListener.onPhotoCancel();
                }
                break;

            //选择图片
            case INTENT_SELECT:
                Log.e(TAG,"选本地图片");
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    if (corp(fragment, imageUri, userPhone, time, photoName)) {
                        return;
                    }
                }
                onPhotoResultListener.onPhotoCancel();
                break;

            //截图
            case INTENT_CROP:
                Log.e(TAG,"开始截图");
                if (resultCode == Activity.RESULT_OK && new File(buildUri(fragment, userPhone, time, photoName).getPath()).exists()) {
                    onPhotoResultListener.onPhotoResult(buildUri(fragment, userPhone, time, photoName));
                }
                break;
        }
    }

    /**
     * 删除文件   上传图片成功后删除源文件
     *
     * @param uri
     * @return
     */
    public boolean clearCropFile(Uri uri) {
        if (uri == null) {
            return false;
        }

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                Log.e(TAG, "Cached crop file cleared.");
            } else {
                Log.e(TAG, "Failed to clear cached crop file.");
            }
            return result;
        } else {
            Log.e(TAG, "Trying to clear cached crop file but it does not exist.");
        }

        return false;
    }

    /**
     * [回调监听类]
     *
     * @author huxinwu
     * @version 1.0
     * @date 2015-1-7
     **/
    public interface OnPhotoResultListener {
        void onPhotoResult(Uri uri);

        void onPhotoCancel();
    }

    public OnPhotoResultListener getOnPhotoResultListener() {
        return onPhotoResultListener;
    }

    public void setOnPhotoResultListener(OnPhotoResultListener onPhotoResultListener) {
        this.onPhotoResultListener = onPhotoResultListener;
    }

}
