package com.yicu.yichujifa.ui.widget;

import android.animation.ValueAnimator;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieqing.codeutils.util.ScreenUtils;
import com.yicu.yichujifa.GlobalContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;

public class FloatWebView extends FloatWindow {
    private LinearLayout rootView;

    WebView webView;

    @BindView(R.id.title)
    TextView title;


    @OnClick(R.id.gone)
    void out(){
        closeWithAnim();
    }


    protected FloatWebView(){
        rootView = (LinearLayout) View.inflate(GlobalContext.getContext(), R.layout.float_webview,null);
        ButterKnife.bind(this,rootView);
        setId("FloatWebView");
        setAllowMove(false);

        setParams(new WindowManager.LayoutParams(-1,-1, MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,-3));



        webView = new WebView(GlobalContext.getContext()){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    closeWithAnim();
                    return true;
                }
                return super.dispatchKeyEvent(event);
            }
        };
        webView.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                title.setText(view.getTitle());
            }
        });
        rootView.addView(webView);
        setView(rootView);
    }

    private boolean isAnim = false;
    private void closeWithAnim() {
        if (isAnim){
            return;
        }
        isAnim = true;
        int ScreenHeight = ScreenUtils.getScreenHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,ScreenHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value >= ScreenHeight){
                    close();
                    isAnim = false;
                }else{
                    rootView.setY(value);
                }
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    private void showWithAnim() {
        if (isAnim){
            return;
        }
        isAnim = true;
        int ScreenHeight = ScreenUtils.getScreenHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(ScreenHeight,0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value <= 0){
                    isAnim = false;
                    rootView.setY(0);
                }else{
                    rootView.setY(value);
                }
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }


    public void show(String data) {
        title.setText("");
        webView.removeAllViews();
        webView.loadDataWithBaseURL(null,data,"text/html","UTF-8",null);
        add();
        showWithAnim();
    }

    private static FloatWebView instance;

    public static FloatWebView getInstance(){
        if (instance == null){
            instance = new FloatWebView();
        }
        return instance;
    }
}
