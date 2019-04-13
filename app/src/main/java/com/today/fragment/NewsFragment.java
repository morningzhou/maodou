package com.today.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.today.R;
import com.today.base.BaseFragment;
import com.today.base.MyApplication;

public class NewsFragment extends BaseFragment {

    private WebView mWebview;
    private final String NEW_WEB_URL = "http://5g.baizhan.net/153/";
    private ImageView mImageView;

    @Override
    protected void findView(View view) {
        mImageView = view.findViewById(R.id.error_load_image);
        mWebview = view.findViewById(R.id.news_web);
        WebSettings settings = mWebview.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.supportMultipleWindows();
        settings.setAllowContentAccess(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSavePassword(true);
        settings.setSaveFormData(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        mWebview.canGoBack();
        mWebview.setWebViewClient(new WebViewClient());
        initListener();

    }

    @Override
    public View initView() {
        return View.inflate(MyApplication.getContext(), R.layout.fragment_news, null);
    }

    @Override
    public void initData() {
        try {
            mWebview.loadUrl(NEW_WEB_URL);
        }catch (Exception e){

        }
    }

    @Override
    public void initListener() {
        mWebview.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebview.canGoBack()) {
                    mWebview.goBack();
                    return true;
                }
                return false;
            }

        });
    }


}
