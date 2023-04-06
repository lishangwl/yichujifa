package esqeee.xieqing.com.eeeeee.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.SnackbarUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.listener.FragmentOnKeyListener;
import esqeee.xieqing.com.eeeeee.ui.EditorActivity;
import esqeee.xieqing.com.eeeeee.user.User;

public class BBSFragment extends BaseFragment implements FragmentOnKeyListener {
    @BindView(R.id.webView)
    WebView webView ;

    @BindView(R.id.floatButton)
    FloatingActionButton floatingActionButton;


    SwipeRefreshLayout swipeRefreshLayout;

    private Map<String,View.OnClickListener> listenerMap = new HashMap<>();

    @Override
    public View getContentView(LayoutInflater inflater) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_shequ,null);
        return swipeRefreshLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listenerMap.put("addAcrtle",v->{
           startActivity(new Intent(getBaseActivity(),EditorActivity.class));
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





    @SuppressLint("JavascriptInterface")
    @Override
    protected void onFragmentInit() {

        swipeRefreshLayout.setOnRefreshListener(()->{
            webView.reload();
        });
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getScheme().equals("http")||uri.getScheme().equals("https")){
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
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        webView.addJavascriptInterface(this,"ycf");
        webView.loadUrl("http://192.168.0.111");

    }

    @JavascriptInterface
    public void login(){
        getBaseActivity().runOnUiThread(()->{

        });
    }

    @JavascriptInterface
    public void showFab(String icon,String listener){
        getBaseActivity().runOnUiThread(()->{
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setImageResource(getBaseActivity().getResources().getIdentifier(icon,"mipmap",getBaseActivity().getPackageName()));
            floatingActionButton.setOnClickListener(listenerMap.get(listener));
        });
    }

    @JavascriptInterface
    public void hideFab(){
        getBaseActivity().runOnUiThread(()->{
            floatingActionButton.setVisibility(View.GONE);
        });
    }

    @JavascriptInterface
    public void snackbar(String message){
        getBaseActivity().runOnUiThread(()->{
            SnackbarUtils.with(webView).setMessage(message).show();
        });
    }

    @JavascriptInterface
    public boolean isLogin(){
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (webView.canGoBack()){
                webView.goBack();
                return true;
            }
        }
        return false;
    }
}
