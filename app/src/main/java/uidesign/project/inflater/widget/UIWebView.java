package uidesign.project.inflater.widget;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import uidesign.project.inflater.WebViewInflater;
import uidesign.project.model.Attr;

import static uidesign.project.inflater.BaseLayoutInflater.ATTR_ON_CREATE;

public class UIWebView extends WebView {
    Attr attr;
    public UIWebView(Context context, Attr attr) {
        super(context);
        this.attr = attr;
        getSettings().setJavaScriptEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setWebChromeClient(new WebChromeClient(){

        });
        setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://")||url.startsWith("https://")){
                    view.loadUrl(url);
                }else{
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }catch (ActivityNotFoundException e){

                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                String value = attr.getString(WebViewInflater.ATTR_WEB_ONLOAD_COMPLETE);
                if (!value.equals("") &&!value.equals("无")){
                    ActionRunHelper.startAction(getContext(),value.trim());
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                String value = attr.getString(WebViewInflater.ATTR_WEB_ONLOAD_FIAL);
                if (!value.equals("") &&!value.equals("无")){
                    ActionRunHelper.startAction(getContext(),value.trim());
                }
            }
        });
        addJavascriptInterface(this,"ycf");
    }

    @Override
    public void loadUrl(String url) {
        attr.put(WebViewInflater.ATTR_WEB_URL,url);
        super.loadUrl(url);
    }

    @JavascriptInterface
    public void jsAndroid(String param){
        attr.put("jsParam",param);
        String value = attr.getString(WebViewInflater.ATTR_WEB_JS_EVENT);
        if (!value.equals("") &&!value.equals("无")){
            ActionRunHelper.startAction(getContext(),value.trim());
        }
    }

    public String getJsParam() {
        return attr.has("jsParam")?attr.getString("jsParam"):"";
    }
}
