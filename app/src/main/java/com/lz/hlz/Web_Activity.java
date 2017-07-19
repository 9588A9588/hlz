package com.lz.hlz;

import android.Manifest;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lz.hlz.widget.TitleActivity;
import com.lz.hlz.dialog.LoadingDialog;

public class Web_Activity extends TitleActivity {
    //http://www.lovetb233.com/youhui
    private WebView webView;
    private LoadingDialog dialog;
    private View mErrorView;
    boolean temp = true;
    boolean mIsErrorPage;
    private Button mBackwardbButton;
    private RelativeLayout mErrorPage;
    private RelativeLayout mAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //动态获取网络权限
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.INTERNET
                },
                10000
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mBackwardbButton = (Button) findViewById(R.id.button_backward);
        setTitle("小肥羊");
        mIsErrorPage = false;
        showBackwardView(R.string.gps_button, true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back_arrow);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBackwardbButton.setCompoundDrawables(null, drawable, null, null);  //设置按钮上下左右图片
        setBackButtonTitle("");
        dialog = new LoadingDialog(this, "加载中...请稍候", R.raw.load, LoadingDialog.Type_GIF);
        mErrorPage = (RelativeLayout) findViewById(R.id.online_error_btn_retry);
        mAll = (RelativeLayout) findViewById(R.id.rly_all);
        webView = (WebView) findViewById(R.id.webview);
        //沉浸式风格  需要先修改values/styles/AppTheme为Theme.AppCompat.LightNoActionBar（隐藏标题）
        Window window = getWindow();
        window.setFlags(
                //透明状态栏
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                //透明导航栏
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置webview支持js
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//禁止缓存
        webSettings.setDomStorageEnabled(true);//开启DOM储存API
        //屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件:
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        String url = bundle.getString("url");
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub

                //      super.onPageFinished(view, url);

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

        });
        //重新重载一次WebViewClient，用于解决重定向时404的bug
        WebViewClient client = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                disGifdialog();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showGifdialog(view);
                super.onPageStarted(view, url, favicon);

            }


            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return;
                }
                // 在这里显示自定义错误页
                mIsErrorPage = true;
                mErrorPage.setVisibility(View.VISIBLE);
            }

            // 新版本，只会在Android6及以上调用
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                    // 在这里显示自定义错误页
                    mErrorPage.setVisibility(View.VISIBLE);
                    mIsErrorPage = true;
                }

            }


        };

        webView.setWebViewClient(client);
        webView.setEnabled(false);// 当加载网页的时候将网页进行隐藏

        mErrorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
                if (mIsErrorPage == true) {
                    mErrorPage.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    mIsErrorPage = false;
                }
            }
        });
    }

    //按返回键时， 不退出程序而是返回上一浏览页面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        System.exit(0);
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 显示进度对话框
     *
     * @param v
     */
    public void showGifdialog(View v) {
        dialog.show();

//        }
    }


    public void disGifdialog() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    @Override
    protected void onPause() {
        webView.pauseTimers();
        super.onPause();


    }

    @Override
    protected void onResume() {
        webView.resumeTimers();
        super.onResume();

    }

    /**
     * 返回按钮点击后触发
     *
     * @param backwardView
     */
    protected void onBackward(View backwardView) {
        super.onBackward(backwardView);
        finish();
    }

}
