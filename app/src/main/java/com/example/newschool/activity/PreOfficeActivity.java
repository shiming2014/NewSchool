package com.example.newschool.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.example.newschool.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PreOfficeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    private ActionBar actionBar;
    private WebSettings webSettings;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_office);
        initView();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.homework_preview,null));
        }
    }
    public static Boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }

            }
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 为toolbar创建Menu
        getMenuInflater().inflate(R.menu.homeworkmenu, menu);
        return true;
    }

    private static Boolean isPDF(String url) {
        if (url.endsWith(".pdf") || url.endsWith(".PDF"))
            return true;
        return false;
    }

    private static Boolean isOFFICE(String url) {
        if (url.endsWith(".doc") || url.endsWith(".docx") || url.endsWith(".ppt") || url.endsWith(".pptx") || url.endsWith(".xlsx") || url.endsWith(".xls"))
            return true;
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //TODO 菜单的逻辑实现
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
    private void initView() {
        url=getIntent().getStringExtra("url");
        toolbar = findViewById(R.id.homework_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                return false;
            }
        });
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        webView = findViewById(R.id.homework_Online);
        webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        if (checkNetwork(getApplicationContext())) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        try {
            String a = URLEncoder.encode(url, "iso-8859-1");

            if (isPDF(a)) {
                webView.loadUrl("file:///android_asset/pdfjs/web/viewer.html?file=" + a);
            } else if(isOFFICE(a)){
                webView.loadUrl("http://view.officeapps.live.com/op/view.aspx?src=" + a);
            }



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}
