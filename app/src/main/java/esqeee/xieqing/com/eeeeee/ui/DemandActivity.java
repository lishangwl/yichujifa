package esqeee.xieqing.com.eeeeee.ui;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.ToastUtils;

import esqeee.xieqing.com.eeeeee.R;

public class DemandActivity extends BaseActivity {

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broswer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        toolbar.setTitle("提交开发需求");
        getSupportActionBar().setTitle("提交开发需求");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String url = getIntent().getStringExtra("url");

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient(){
//
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return false;//super.onJsAlert(view, url, message, result);
//            }
//        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                Log.d("BroswerActivityTAG",url);
                if (uri.getScheme().equals("http")||uri.getScheme().equals("https")||uri.getScheme().equals("file")){
                    view.loadUrl(url);
                    return true;
                }else{
                    try {
                        startActivity(IntentUtils.getUrlIntent(url));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //getSupportActionBar().setTitle(view.getTitle());
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser,menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (((WebView)findViewById(R.id.webView)).canGoBack()){
                ((WebView)findViewById(R.id.webView)).goBack();
                return false;
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.web_copy:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                clipboardManager.setText(((WebView)findViewById(R.id.webView)).getUrl());
                ToastUtils.showShort("已复制");
                break;
            case R.id.web_open:
                startActivity(IntentUtils.getUrlIntent(((WebView)findViewById(R.id.webView)).getUrl()));
                break;
            case R.id.web_refresh:
                ((WebView)findViewById(R.id.webView)).reload();
                break;
        }
        return true;
    }

    public static void luanch(Context context, String url){
        Intent intent = new Intent(context, DemandActivity.class);
        intent.putExtra("url",url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void luanchResource(Context context, String url){
        Intent intent = new Intent(context, DemandActivity.class);
        intent.putExtra("url","file:///android_asset/"+url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        WebView webView = findViewById(R.id.webView);
        webView.removeAllViews();
        webView.clearView();
        webView.destroy();
    }
}
