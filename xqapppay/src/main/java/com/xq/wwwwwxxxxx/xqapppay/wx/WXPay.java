package com.xq.wwwwwxxxxx.xqapppay.wx;

import android.content.Context;
import android.util.Log;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayListener;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayResult;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PrePayCallBack;
import com.xq.wwwwwxxxxx.xqapppay.wx.task.GetPrepayIdTask;
import com.xq.wwwwwxxxxx.xqapppay.wx.utils.MD5;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WXPay {
    static WXPay wxPay;
    private String appId = "";
    private String appKey = "";
    private String macId = "";
    private String notifyUrl = "";

    private IWXAPI msgApi;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getMacId() {
        return macId;
    }

    public String getAppId() {
        return appId;
    }

    public static WXPay getWxPay() {
        if (wxPay == null){
            wxPay = new WXPay();
        }
        return wxPay;
    }

    public void init(String appId, String appKey, String macId,String notifyUrl){
        this.appId = appId;
        this.appKey = appKey;
        this.macId = macId;
        this.notifyUrl = notifyUrl;
    }

    private PayListener listener;

    public PayListener getListener() {
        return listener;
    }

    public void pay(final Context context, final WXPayReq req, final PayListener listener){
        this.listener = listener;
        new GetPrepayIdTask(new PrePayCallBack() {
            @Override
            public void onResult(boolean isCompleted) {
                if (!isCompleted){
                    listener.onResult(PayResult.ERROR,"生成订单失败");
                    return;
                }
                if (msgApi == null){
                    msgApi = WXAPIFactory.createWXAPI(context, null);
                    msgApi.registerApp(getAppId());
                }
                msgApi.sendReq(req.getReq());
            }
        }, req).execute();
    }
    public static final class NameValuePair{
        private String name;
        private String value;
        NameValuePair(String name,String value){
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
    public void payWithPrepayId(final Context context, final WXPayReq wxPayReq, final PayListener listener){
        this.listener = listener;
        if (msgApi == null){
            msgApi = WXAPIFactory.createWXAPI(context, getAppId(),false);
            boolean result = msgApi.registerApp(getAppId());
            boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            Log.d("WX","registerApp=>"+result);
            Log.d("WX","isPaySupported=>"+isPaySupported);
        }

        PayReq req = new PayReq();
        req.appId = WXPay.getWxPay().getAppId();
        req.partnerId = WXPay.getWxPay().getMacId();
        req.prepayId = wxPayReq.getPrepayId();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        List linkedList = new LinkedList();
        linkedList.add(new NameValuePair("appid", req.appId));
        linkedList.add(new NameValuePair("noncestr", req.nonceStr));
        linkedList.add(new NameValuePair("package", req.packageValue));
        linkedList.add(new NameValuePair("partnerid",req.partnerId));
        linkedList.add(new NameValuePair("prepayid", req.prepayId));
        linkedList.add(new NameValuePair("timestamp",req.timeStamp));
        req.sign = genAppSign(linkedList);

        wxPayReq.setReq(req);

        msgApi.sendReq(req);
    }

    private String genAppSign(List<NameValuePair> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append(((NameValuePair) list.get(i)).getName());
            stringBuilder.append('=');
            stringBuilder.append(((NameValuePair) list.get(i)).getValue());
            stringBuilder.append('&');
        }
        stringBuilder.append("key=");
        stringBuilder.append(WXPay.getWxPay().getAppKey());
        String toUpperCase = MD5.getMessageDigest(stringBuilder.toString().getBytes()).toUpperCase();
        Log.e("orion", toUpperCase);
        return toUpperCase;
    }
}
