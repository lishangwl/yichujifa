package com.xq.wwwwwxxxxx.xqapppay.wx;

import android.os.Bundle;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.utils.Log;
import com.tencent.mm.opensdk.utils.a;

public class WXPayReq {
    private String appId;
    private String prepayId;
    private String body;
    private String price = "0";

    private PayReq req;

    public void setReq(PayReq req) {
        this.req = req;
    }

    public PayReq getReq() {
        return req;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPrepayId() {
        return prepayId;
    }
}
