package com.fala.emba.team.base.usage;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fala.emba.team.R;
import com.fala.emba.team.app.MyApplication;
import com.fala.emba.team.base.fragment.BaseFragment;
import com.fala.emba.team.base.webview.BaseWebView;

import com.fala.emba.team.constant.Consts;
import com.fala.emba.team.constant.RequestConst;
import com.fala.emba.team.download.MimeTest;
import com.fala.emba.team.util.FileUtil;
import com.fala.emba.team.util.LogManager;
import com.fala.emba.team.util.SharePrefUtil;
import com.fala.emba.team.util.ToastManager;
import com.fala.emba.team.util.TokenManager;
import com.fala.emba.team.util.UrlUtil;
import com.google.gson.Gson;
import com.yy.http.okhttp.OkHttpUtils;
import com.yy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.HttpUrl;


/**
 * FragmentWebView基类
 * Created by SoMustYY on 2017/7/22.
 */
@SuppressLint({"JavascriptInterface", "NewApi"})
public abstract class BaseWebViewFragment1 extends BaseFragment {
    private static final String TAG = "BaseWebViewFragment1";
    private Activity mActivity;

    private FrameLayout flBack;
    private TextView mRightTv;

    private BaseWebView mWebView;

    private ProgressBar progressbar;  //加载动画

    private String url;

    private String webUrl = null;


    private WebViewClient mWebViewClient;

    private WebChromeClient mWebChromeClient;

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>

    private Uri imageUri;

    private DownloadManager manager;
    private BroadcastReceiver receiver;

    private ProgressDialog progressDialog;
    private String fileName;

    private RelativeLayout view_progress;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        setContentView(getLayoutId());  //填充布局


        inits();
        url = setUrl();
        flBack = findView(R.id.fl_back);
        mRightTv = findView(R.id.tv_title_right);
        mWebView = findView(R.id.progress_webview);

        view_progress = findView(R.id.view_progress);

        initProgressBar(mActivity);
        initWebViewClient();
        initWebChromeClient();

        if (isShowLeft()) {
            flBack.setVisibility(View.VISIBLE);
        } else {
            flBack.setVisibility(View.INVISIBLE);
        }
        if (isShowRightTv()) {
            mRightTv.setVisibility(View.VISIBLE);
        } else {
            mRightTv.setVisibility(View.INVISIBLE);
        }
        //session保持
//        if (url != null) {
//            if (isAddSession()) {
//                synCookies(getActivity(), url);
//            }
//
//        }
//        mWebView.setVisibility(View.INVISIBLE);

        initDownLoad();

        if (mWebView != null && url != null) {
            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("下载中，请稍后。。。。");

            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setSupportZoom(true);
            mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
            mWebView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                    LogManager.e("webView下载测试：", "点击下载");
                    LogManager.e("webView下载测试 url=：", url);
                    LogManager.e("webView下载测试 userAgent=：", userAgent);
                    LogManager.e("webView下载测试 contentDisposition=：", contentDisposition);
                    LogManager.e("webView下载测试 mimetype=：", mimetype);
                    LogManager.e("webView下载测试 contentLength=：", contentLength + "");

                    String content = "文件大小:" + String.format("%.1f", (float) contentLength / 1024) + "KB";
                    //showDownloadDialog(url, userAgent, contentDisposition, mimetype, content, contentLength + "");


                }
            });
            mWebView.setWebViewClient(mWebViewClient);
            mWebView.setWebChromeClient(mWebChromeClient);
        }
        //token的动态获取
//        if (webUrl == null) {
//            String tokenFlag = TokenManager.checkToken(mActivity);
//            switch (tokenFlag) {
//                case "token_null":   //token为空
//                    obtainWebViewToken(mActivity);
//                    break;
//                case "token_overtime":   //token超时
//                    obtainWebViewToken(mActivity);
//                    break;
//                case "token_normal":   //token正常
//                    String token = SharePrefUtil.getString(mActivity, Consts.TOKEN, "null");
//                    webUrl = url + "token=" + token;
//                    mWebView.loadUrl(webUrl);
//                    LogManager.e(TAG, "webview加载url=" + webUrl);
//                    break;
//            }
//
//        }


    }


//    public void ReloadWebView() {
//        mWebView.setVisibility(View.INVISIBLE);
//        String tokenFlag = TokenManager.checkToken(mActivity);
//        switch (tokenFlag) {
//            case "token_null":   //token为空
//                obtainWebViewToken(mActivity);
//                break;
//            case "token_overtime":   //token超时
//                obtainWebViewToken(mActivity);
//                break;
//            case "token_normal":   //token正常
//                String token = SharePrefUtil.getString(mActivity, Consts.TOKEN, "null");
//                webUrl = url + "token=" + token;
//                mWebView.loadUrl(webUrl);
//                LogManager.e(TAG, "webview加载url=" + webUrl);
//                break;
//        }
////        mWebView.reload();
//    }


//    /**
//     * 通过登录获取token
//     *
//     * @param context
//     * @return
//     */
//    public void obtainWebViewToken(final Context context) {
//        String logincode = SharePrefUtil.getString(context, Consts.LOGINCODE, null);
//        String password = SharePrefUtil.getString(context, Consts.PASSWORD, null);
//
//        Map<String, String> params = new HashMap<>();
//        params.put("loginCode", logincode);
//        params.put("passWord", password);
//        OkHttpUtils
//                .post()
//                .url(RequestConst.APP_LOGIN)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        LogManager.e("WebView", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        UserComm userComm = new Gson().fromJson(response, UserComm.class);
//                        if (userComm.getStatus().equals(RequestConst.APP_STATUS_T)) {
//                            LogManager.e(TAG, "WEB_TOKEN=" + userComm.getToken());
//                            SharePrefUtil.saveString(context, Consts.TOKEN, userComm.getToken());//保存获取到的token
//                            webUrl = url + "token=" + userComm.getToken();
//                            mWebView.loadUrl(webUrl);
//                            LogManager.e(TAG, "webview加载url=" + webUrl);
//                        }
//                    }
//                });
//    }

    /**
     * 初始化下载相关
     */
    private void initDownLoad() {
        manager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {  //下载完成
                    long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downId);
                    Cursor c = ((DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE)).query(query);
                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch (status) {
                            case DownloadManager.STATUS_SUCCESSFUL:
                                //完成
                                Log.v("down", "下载完成");
                                if (downId == myReference) {
                                    ShowMsg("下载完成");
                                    openFile();
                                }
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                Log.v("down", "STATUS_PAUSED");
                                break;
                            case DownloadManager.STATUS_PENDING:
                                Log.v("down", "STATUS_PENDING");
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                //正在下载，不做任何事情
                                Log.v("down", "STATUS_RUNNING");
                                ShowMsg("正在下载...");
                                break;
                            case DownloadManager.STATUS_FAILED:
                                Log.v("down", "STATUS_FAILED");
                                ShowMsg("下载文件失败，文件不存在");
                                break;
                        }
                    }
                }
            }

        };
        mActivity.registerReceiver(receiver, filter);
    }


    private void openFile() {
        Uri uri = manager.getUriForDownloadedFile(myReference);
        Log.e("MYTAG", uri.toString());
        String path = getRealFilePath(mActivity, uri);
        Log.e("MYTAG", "path=" + path);
        Intent intent2 = new Intent();
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent2.setAction(Intent.ACTION_VIEW);
        //设置intent的data和Type属性。
        //获取文件file的MIME类型
        File file = new File(path);
        String type = MimeTest.getMIMEType(file);

        intent2.setDataAndType(Uri.fromFile(file), type);
        //跳转
        try {
            startActivity(intent2);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    public static String getRealFilePath(final Context context, final Uri uri) {

        if (null == uri) return null;

        final String scheme = uri.getScheme();
        String data = null;

        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 显示Toast提示信息
     *
     * @param pMsg （String）
     */
    public void ShowMsg(String pMsg) {
        ToastManager.showShortText(mActivity, pMsg);
    }


    private String myMimetype;
    private long myReference;

//    public void showDownloadDialog(final String url, final String userAgent, final String contentDisposition, final String mimetype, final String content, final String contentLength) {
//        View v = LayoutInflater.from(mActivity).inflate(
//                R.layout.dialog_download_notes, null);
//        final AlertDialog alertDialog = getDialogView(mActivity, v);
//        TextView closetv = v.findViewById(R.id.dialog_close_tv);
//        TextView showtv = v.findViewById(R.id.dialog_show_tv);
//        TextView contenttv = v.findViewById(R.id.dialog_content_tv);
//        contenttv.setText(content);
//        closetv.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                alertDialog.dismiss();
//            }
//        });
//
//        showtv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DownloaderTask().execute(url, userAgent, contentDisposition, mimetype, contentLength);//开启下载进度
//                alertDialog.dismiss();
//                myMimetype = mimetype;
//
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                request.setMimeType(mimetype);
//                String cookies = CookieManager.getInstance().getCookie(url);
//                request.addRequestHeader("cookie", cookies);
//                request.addRequestHeader("User-Agent", userAgent);
//                request.setDescription(url);
//                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
//                request.allowScanningByMediaScanner();
//
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
//                fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
//
//                request.setDestinationInExternalPublicDir(FileUtil.getCommonPath(), fileName);//设置下载路径
//                //request.setDestinationInExternalFilesDir(mActivity, FileUtil.getCommonPath(), fileName);//设置下载路径
//                DownloadManager dm = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
//                dm.enqueue(request);
//                myReference = manager.enqueue(request);
//
//            }
//        });
//    }

    /**
     * 返回一个自定义View对话框
     */
    public AlertDialog getDialogView(Context cxt, View view) {
        AlertDialog dialog = new AlertDialog.Builder(cxt).create();
        mActivity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;

    }

    @SuppressLint("ResourceAsColor")
    private void initProgressBar(Context context) {

        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, dp2px(context, 2), AbsoluteLayout.LayoutParams.MATCH_PARENT, AbsoluteLayout.LayoutParams.MATCH_PARENT);

        progressbar.setLayoutParams(params);
        //改变progressbar默认进度条的颜色（深红色）为Color.GREEN
        progressbar.setProgressDrawable(new ClipDrawable(new ColorDrawable(Color.parseColor("#04d06f")), Gravity.LEFT, ClipDrawable.HORIZONTAL));
//        mWebView.addView(progressbar);

        view_progress.addView(progressbar);
        //此段代码不可少，将背景色更换为透明色，否则显示背景图片以及progressDialog不会显示
//        mWebView.setBackgroundColor(android.R.color.transparent);
//        //更换背景图片
//        mWebView.setBackgroundResource();
    }



    //cookie保持
    @SuppressWarnings("deprecation")
    public static void synCookies(Context context, String url) {
        //CookieSyncManager负责管理webView中的cookie
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = MyApplication.getInstance().getCookieJar().loadForRequest(HttpUrl.parse(url)); //获取okhttp网络请求中持久化的cookie
        StringBuffer sb = new StringBuffer();
        for (Cookie cookie : cookies) {
            String cookieName = cookie.name();
            String cookieValue = cookie.value();
            sb.append(cookieName + "=");
            sb.append(cookieValue + ";");
        }
        String[] cookie = sb.toString().split(";");
        String[] sessionId = sb.toString().split("=");
        LogManager.d("NAME：", sessionId.toString());
//        LogManager.d("NAME：", sessionId[1]);
//        LogManager.d("SESSION_ID：", sessionId[1]);
        // SharePrefUtil.saveString(context, "SESSION_ID", cookieValue);  //保存cookie到本地
        for (int i = 0; i < cookie.length; i++) {
            Log.e("cookie" + i, cookie[i]);
            cookieManager.setCookie(url, cookie[i]);// cookies是在HttpClient中获得的cookie
        }
        CookieSyncManager.getInstance().sync();
        //checkSession(cookieManager, cookie, url);

    }

    private static void checkSession(final CookieManager cookieManager, final String[] cookie, final String url) {
        OkHttpUtils
                .post()
                .url(RequestConst.APP_SERVER)
                .addParams("sessionId", cookie[0])
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        for (int i = 0; i < cookie.length; i++) {
                            Log.e("cookie" + i, cookie[i]);
                            cookieManager.setCookie(url, cookie[i]);// cookies是在HttpClient中获得的cookie
                        }

                        CookieSyncManager.getInstance().sync();
                    }
                });
    }


    public void onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); //goBack()表示返回WebView的上一页面
        } else {
            ToastManager.showShortText(mActivity, "无法返回，当前已是模块首页");
        }

    }




    /**
     * 默认自己应用跳转网页
     */
    private void initWebViewClient() {
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                mWebView.setVisibility(View.INVISIBLE);
                view.loadUrl(url);

                // 把当前页面的地址赋值到全局变量，方便监听
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
            }
        };
    }

    Handler handler = new Handler();

    private void initWebChromeClient() {
        mWebChromeClient = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {

                    //延迟加载
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.setVisibility(View.VISIBLE);
                            progressbar.setVisibility(View.GONE);
                        }
                    }, 100);

                } else {
                    if (progressbar.getVisibility() == View.GONE) {
                        progressbar.setVisibility(View.VISIBLE);
                    }
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            // For Android 3.0-
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.d(TAG, "onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) ");
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                return true;
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            Log.e("result", result + "");
            String stringResult = result + "";
            result = Uri.parse(UrlUtil.getURLDecoderString(stringResult));
            imageUri = Uri.parse(UrlUtil.getURLDecoderString(stringResult));
            Log.e("dresult", result + "");
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {

                if (result == null) {
//	            	mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }

        return;
    }

    /**
     * 方法描述：根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(receiver);
        if (myReference > 0) {
            manager.remove(myReference);
        }
    }


    //内部类
    private class DownloaderTask extends AsyncTask<String, Integer, String> {


        private String doDownload(String... strings) {
            Log.d(TAG, "urlString:" + strings[0] + "\nuserAgent: " + strings[1] + "\ncontentDisposition: " + strings[2] + "\nmimeType: " + strings[3]);
            String fileName = strings[2].substring(strings[2].lastIndexOf("=") + 1);
            long s = Long.parseLong(strings[4].trim());
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(8000);
                httpURLConnection.setReadTimeout(8000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("attachment", strings[2].substring(strings[2].lastIndexOf("; ") + 2));
//                httpURLConnection.setRequestProperty("Referer", editText.getText().toString().trim());
                httpURLConnection.setRequestProperty("User-Agent", strings[1]);

                InputStream inputStream = httpURLConnection.getInputStream();
                FileOutputStream fos = mActivity.openFileOutput(fileName, Context.MODE_PRIVATE);
                byte[] b = new byte[2048];
                int j = 0;
                long now = 0;
                while ((j = inputStream.read(b)) != -1) {
                    publishProgress((int) (100 * now / s));//publishProgress();//调用此方法更新Progress
                    now += j;
                    fos.write(b, 0, j);
                }
                fos.flush();
                fos.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileName;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileName = doDownload(strings);
            return fileName;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
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

    /**
     * 设置URL
     */
    protected abstract String setUrl();


    /**
     * 是否显示返回键
     *
     * @return
     */
    protected boolean isShowLeft() {
        return true;
    }



    /**
     * 是否显示右边不带图片textview
     *
     * @return
     */
    protected boolean isShowRightTv() {
        return true;
    }

    /**
     * 是否头部添加Session
     *
     * @return
     */
    protected boolean isAddSession() {
        return true;
    }

}
