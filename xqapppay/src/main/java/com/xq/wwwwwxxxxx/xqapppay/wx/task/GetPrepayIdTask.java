package com.xq.wwwwwxxxxx.xqapppay.wx.task;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPay;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPayReq;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PrePayCallBack;
import com.xq.wwwwwxxxxx.xqapppay.wx.utils.HttpUtils;
import com.xq.wwwwwxxxxx.xqapppay.wx.utils.MD5;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {
    WXPayReq wxPayReq;
    PrePayCallBack prePayCallBack;
    public GetPrepayIdTask(PrePayCallBack prePayCallBack,WXPayReq wxPayReq) {
        this.wxPayReq = wxPayReq;
        this.prePayCallBack = prePayCallBack;
    }

    protected void onPostExecute(Map<String, String> map) {
        if (map.containsKey("prepay_id")){
            wxPayReq.setPrepayId(map.get("prepay_id"));

            PayReq req = new PayReq();
            req.appId = WXPay.getWxPay().getAppId();
            req.partnerId = WXPay.getWxPay().getMacId();
            req.prepayId = map.get("prepay_id");
            req.packageValue = "Sign=WXPay";
            req.nonceStr = genNonceStr();
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

            prePayCallBack.onResult(true);
        }else{
            prePayCallBack.onResult(false);
        }
    }

    protected void onCancelled() {
        prePayCallBack.onResult(false);
    }

    private String genNonceStr() {
        return MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
    }


    private String genProductArgs() {
        try {
            List linkedList = new LinkedList();
            linkedList.add(new NameValuePair("appid",WXPay.getWxPay().getAppId()));
            linkedList.add(new NameValuePair("body", wxPayReq.getBody()));
            linkedList.add(new NameValuePair("mch_id", WXPay.getWxPay().getMacId()));
            linkedList.add(new NameValuePair("nonce_str", genNonceStr()));
            linkedList.add(new NameValuePair("notify_url", WXPay.getWxPay().getNotifyUrl()));
            linkedList.add(new NameValuePair("out_trade_no", genOutTradNo()));
            linkedList.add(new NameValuePair("spbill_create_ip", "127.0.0.1"));
            linkedList.add(new NameValuePair("total_fee", wxPayReq.getPrice()));
            linkedList.add(new NameValuePair("trade_type", "APP"));
            linkedList.add(new NameValuePair("sign", genPackageSign(linkedList)));
            return toXml(linkedList);
        } catch (Exception e) {
            return null;
        }
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


    private String genPackageSign(List<NameValuePair> list) {
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
        Log.e("WXPAY", stringBuilder.toString());
        return toUpperCase;
    }




    private String genOutTradNo() {
        return MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
    }


    private String toXml(List<NameValuePair> list) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<xml>");
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append("<" + ((NameValuePair) list.get(i)).getName() + ">");
            stringBuilder.append(((NameValuePair) list.get(i)).getValue());
            stringBuilder.append("</" + ((NameValuePair) list.get(i)).getName() + ">");
        }
        stringBuilder.append("</xml>");
        Log.e("orion", stringBuilder.toString());
        return stringBuilder.toString();
    }


    public Map<String, String> decodeXml(String str) {
        try {
            Map<String, String> hashMap = new HashMap();
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setInput(new StringReader(str));
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                String name = newPullParser.getName();
                switch (eventType) {
                    case 2:
                        if (!"xml".equals(name)) {
                            hashMap.put(name, newPullParser.nextText());
                            break;
                        }
                        break;
                    default:
                        break;
                }
            }
            return hashMap;
        } catch (Exception e) {
            return null;
        }
    }


    protected Map<String, String> doInBackground(Void... voidArr) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String orion = genProductArgs();
        String result = HttpUtils.Post(url, orion);
        Log.d("WXPAY","data ->"+orion);
        Log.d("WXPAY","result ->"+result);
        return decodeXml(result);
    }
}
