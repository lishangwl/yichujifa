package esqeee.xieqing.com.eeeeee.action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

import com.xieqing.codeutils.util.LogUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;


public class ActionUtils {
    public static void toWeChatScanDirect(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtils.showShort("无法跳转到微信，请检查是否安装了微信");
            e.printStackTrace();
        }
    }
    public static void toMobileQQChat(Context context,String qq) {
        try {
            //利用Intent打开微信
            Uri uri = Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+qq);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            //若无法正常跳转，在此进行错误处理

            ToastUtils.showShort("无法跳转到微信，请检查是否安装了微信");
        }
    }

    /** 支付宝scheme打开界面码
     * 跳过开启动画打开扫码 10000007
     * 爱心捐助 10000009
     * 彩票 10000012
     * 付款码 20000056
     *  收钱码 20000123
    **/
    public static void toAliPay(Context context,int code) {

        try {
            //利用Intent打开支付宝
            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId="+code);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
         } catch (Exception e) {
            //若无法正常跳转，在此进行错误处理

            ToastUtils.showShort("无法跳转到支付宝，请检查是否安装了支付宝");
        }
    }
}
