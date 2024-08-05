package com.example.music_01;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // 延迟2秒
    }

    private void init() {
        WebView webView = (WebView) findViewById(R.id.webview);

        // 需要加载的网页的url
        webView.loadUrl("file:///android_asset/html/1.html"); // 这里写的是assets文件夹下html文件的名称，需要带上后面的后缀名，前面的路径是安卓系统自己规定的android_asset就是表示的在assets文件夹下的意思。

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true); // 自适应屏幕
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true); // 扩大比例的缩放
        webView.getSettings().setBuiltInZoomControls(true); // 设置是否出现缩放工具

        WebSettings settings = webView.getSettings();

        // 如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        settings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

}
