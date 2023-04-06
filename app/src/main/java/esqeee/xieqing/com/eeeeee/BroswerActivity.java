package esqeee.xieqing.com.eeeeee;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alipay.sdk.app.PayTask;
import com.tencent.mobileqq.openpay.api.OpenApiFactory;
import com.tencent.mobileqq.openpay.data.pay.PayApi;
import com.xieqing.codeutils.util.HttpUtils;
import com.xieqing.codeutils.util.IntentUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xq.wwwwwxxxxx.paydialog.PayDialog;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPay;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPayReq;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayResult;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import esqeee.xieqing.com.eeeeee.bbs.NetTask;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.user.User;

public class BroswerActivity extends BaseActivity {



    private void alipay() {
        showProgress("正在生成订单");
        new Thread(()->{
            JSONBean result = new JSONBean(HttpUtils.Get("http://bbs.yicuba.com/public/index.php/user/index/order_alipay?money="+getTag("payMoney")+"&attach="+ User.getUser().getUid()));
            PayTask alipay = new PayTask(self());
            Map<String,String> payResult = alipay.payV2(result.getString("msg"),true);
            disProgress();
            Log.d("Alipay",result.getString("msg"));
            Log.d("Alipay",payResult.toString());
        }).start();

    }

    private void qqPay() {
        showProgress("正在生成订单");
        ThreadUtils.executeByCached(new NetTask<JSONBean>(){
            @Nullable
            @Override
            public JSONBean doInBackground() throws Throwable {
                return new JSONBean(HttpUtils.Get("http://bbs.yicuba.com/public/index.php/user/index/order_qq?fee="+String.valueOf(((int)getTag("payMoney"))*100)+"&attach="+ User.getUser().getUid()+"&body=一触即发会员"));
            }

            @Override
            public void onSuccess(@Nullable JSONBean result){
                disProgress();
                String order = result.getString("order");
                String prepayId = result.getString("prepayId");
                PayApi api = new PayApi();
                api.appId = "1110024676";
                api.serialNumber = order;
                api.callbackScheme = "qwallet1110024676";
                api.tokenId = prepayId;
                api.pubAcc = "";
                api.pubAccHint = "";
                api.nonce = String.valueOf(System.currentTimeMillis());
                api.timeStamp = System.currentTimeMillis() / 1000;
                api.bargainorId = "1584279061";

                // 按key排序
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("appId=").append(api.appId);
                stringBuilder.append("&bargainorId=").append(api.bargainorId);
                stringBuilder.append("&nonce=").append(api.nonce);
                stringBuilder.append("&pubAcc=").append("");
                stringBuilder.append("&tokenId=").append(api.tokenId);

                try {
                    byte[] byteKey = ("csacafedfgqwrewtrwweterygsfgasfa&").getBytes();
                    // 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
                    SecretKey secretKey = new SecretKeySpec(byteKey, "HmacSHA1");
                    // 生成一个指定 Mac 算法 的 Mac 对象
                    Mac mac = Mac.getInstance("HmacSHA1");
                    // 用给定密钥初始化 Mac 对象
                    mac.init(secretKey);
                    byte[] byteSrc = stringBuilder.toString().getBytes("UTF-8");
                    // 完成 Mac 操作
                    byte[] dst = mac.doFinal(byteSrc);
                    // Base64
                    api.sig = Base64.encodeToString(dst, Base64.NO_WRAP);
                    api.sigType = "HMAC-SHA1";


                    OpenApiFactory.getInstance(self(), "1110024676").execApi(api);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void wxPay() {
        showProgress("正在生成订单");
        ThreadUtils.executeByCached(new NetTask<JSONBean>(){
            @Nullable
            @Override
            public JSONBean doInBackground() throws Throwable {
                return new JSONBean(HttpUtils.Get("http://bbs.yicuba.com/public/index.php/user/order?fee="+String.valueOf(((int)getTag("payMoney"))*100)+"&attach="+ User.getUser().getUid()+"&body=一触即发会员"));
            }

            @Override
            public void onSuccess(@Nullable JSONBean result) {
                disProgress();
                WXPayReq wxPayReq = new WXPayReq();
                wxPayReq.setBody("一触即发会员");
                wxPayReq.setPrice(String.valueOf(((int)getTag("payMoney"))*100));//价格
                wxPayReq.setPrepayId(result.getString("prepayId"));
                WXPay.getWxPay().payWithPrepayId(self(),wxPayReq,BroswerActivity.this::onResult);
            }
        });
    }

    public void onResult(PayResult result, String message) {
        ToastUtils.showLong(message);
        EventBus.getDefault().post(User.getUser());
    }

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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String url = getIntent().getStringExtra("url");

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
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
                getSupportActionBar().setTitle(view.getTitle());
            }
        });


        webView.addJavascriptInterface(new Object(){
            @JavascriptInterface
            public void openVip(String money){
                setTag("payMoney",Integer.parseInt(money));
                new PayDialog(self())
                        .setData(Double.parseDouble(getTag("payMoney").toString()) ,1000)//输入支付金额，余额
                        .haveBalance(true)
                        .setListener(new PayDialog.OnPayClickListener() {
                            @Override
                            public void onPayClick(int payType) {
                                switch (payType) {
                                    case PayDialog.ALI_PAY:
                                        alipay();
                                        break;
                                    case PayDialog.WX_PAY:
                                        wxPay();
                                        break;
                                    case PayDialog.BALANCE_PAY:
                                        qqPay();
                                        break;
                                }
                            }
                        }).show();
            }
        },"app");
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
        Intent intent = new Intent(context,BroswerActivity.class);
        intent.putExtra("url",url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void luanchResource(Context context, String url){
        Intent intent = new Intent(context,BroswerActivity.class);
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
