package com.xq.wwwwwxxxxx.xqapppay.wx.call;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram.Resp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xq.wwwwwxxxxx.xqapppay.wx.WXPay;
import com.xq.wwwwwxxxxx.xqapppay.wx.listener.PayResult;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.api = WXAPIFactory.createWXAPI(this, WXPay.getWxPay().getAppId());
        this.api.handleIntent(getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.api.handleIntent(intent, this);
    }

    public void onReq(BaseReq baseReq) {
    }

    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == 5) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                if (WXPay.getWxPay().getListener() != null){
                    WXPay.getWxPay().getListener().onResult(PayResult.SUCCESS
                            ,"支付成功");
                }
            }else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                if (WXPay.getWxPay().getListener() != null){
                    WXPay.getWxPay().getListener().onResult(PayResult.CANCEL,"取消支付");
                }
            } else {
                if (WXPay.getWxPay().getListener() != null){
                    WXPay.getWxPay().getListener().onResult(PayResult.ERROR,"支付失败！");
                }
            }
        }
        //finish();
    }
}