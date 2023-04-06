package uidesign.project.inflater;

import android.content.Context;
import android.webkit.WebView;
import android.widget.ProgressBar;

import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import uidesign.project.inflater.widget.UIWebView;
import uidesign.project.model.Attr;

public class WebViewInflater extends BaseLayoutInflater<UIWebView> {
    public static final String ATTR_WEB_JSENABLE="启用js";
    public static final String ATTR_WEB_URL="链接";
    public static final String ATTR_WEB_DISPATCH_BACK="返回键后退";
    public static final String ATTR_WEB_ONLOAD_COMPLETE="加载完毕";
    public static final String ATTR_WEB_ONLOAD_FIAL="加载失败";
    public static final String ATTR_WEB_JS_EVENT="js接口调用";
    @Override
    public String getName() {
        return "浏览器";
    }
    @Override
    public boolean bindView(UIWebView view, Attr attrs) {
        for (String key:attrs.keySet()){
            switch (key){
                case ATTR_WEB_JSENABLE:
                    view.getSettings().setJavaScriptEnabled(attrs.getBoolean(key));
                    break;
                case ATTR_WEB_URL:
                    view.loadUrl(attrs.getString(key));
                    break;
            }
        }
        return true;
    }



    @Override
    public UIWebView createViewInstance(Context context, Attr attr) {
        UIWebView webView = new UIWebView(context,attr);
        ((BaseActivity)context).addBackPressedListener(()->{
            if (attr.getBoolean(ATTR_WEB_DISPATCH_BACK)){
                if (webView.canGoBack()){
                    webView.goBack();
                    return true;
                }
            }
            return false;
        });
        return webView;
    }

    @Override
    public Attr getBaseAttr() {
        Attr attr = super.getBaseAttr();
        attr.put("宽度","200dp");
        attr.put("高度","200dp");
        attr.put(ATTR_BACKGROUND_COLOR,"#868686");
        attr.put(ATTR_WEB_URL,"");
        attr.put(ATTR_WEB_JSENABLE,"真");
        attr.put(ATTR_WEB_DISPATCH_BACK,"真");
        attr.put(ATTR_NAME,getOneName("浏览器"));
        attr.put(ATTR_WEB_ONLOAD_COMPLETE,"无");
        attr.put(ATTR_WEB_ONLOAD_FIAL,"无");
        attr.put(ATTR_WEB_JS_EVENT,"无");
        return attr;
    }
}
