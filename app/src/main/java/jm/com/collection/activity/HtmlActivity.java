package jm.com.collection.activity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jm.com.collection.R;
import jm.com.collection.data.LoginData;

/**
 * 功能描述：原生界面与Html界面交互
 *
 * @author ShiJiaMing
 * @create： 2019/3/23
 */
public class HtmlActivity extends AppCompatActivity {

    @InjectView(R.id.wb_content)
    WebView webView;

    private String url="http://192.168.1.40:8080/JMCollectionWeb/index.html";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        ButterKnife.inject(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.addJavascriptInterface(new LoginData(this),"LoginData");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.loadUrl("javascript:setVersion('100');");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
