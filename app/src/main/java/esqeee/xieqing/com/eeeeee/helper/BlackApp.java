package esqeee.xieqing.com.eeeeee.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import android.util.Base64;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.DeviceUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.yicu.yichujifa.GlobalContext;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.user.User;

public class BlackApp {
    private static String[] blackApps = new String[0];

    public static void setBlackApps(String[] blackApps) {
        BlackApp.blackApps = blackApps;
    }

    public static boolean isBlack(CharSequence packageName) {
        for (int i = 0; i < blackApps.length; i++) {
            if (blackApps[i].equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void reportError(Action action) {
        WifiManager wifiManager = (WifiManager) GlobalContext.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        JSONBean jsonBean = new JSONBean();
        jsonBean.put("brand", Build.BRAND);
        jsonBean.put("board", Build.BOARD);
        jsonBean.put("product", Build.PRODUCT);
        try {
            jsonBean.put("wifi_address", wifiManager.getConnectionInfo().getMacAddress());
            jsonBean.put("wifi_bssid", wifiManager.getConnectionInfo().getBSSID());
        } catch (Exception e) {
        }
        jsonBean.put("product", Build.PRODUCT);
        jsonBean.put("androidid", DeviceUtils.getAndroidID());
        if (ActivityCompat.checkSelfPermission(GlobalContext.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            jsonBean.put("imei", PhoneUtils.getDeviceId());
        }
        jsonBean.put("os", DeviceUtils.getSDKVersionName());
        jsonBean.put("user", User.getUser().getName());
        jsonBean.put("uid", User.getUser().getUid());
        jsonBean.put("targetPackage", AppUtils.getTopPackageName());
        jsonBean.put("actionName", action.getTitle());
        BBS.post(BBS.host+"/action/error/report","content="+ Base64.encodeToString(jsonBean.toString().getBytes(),0),null);
    }
}
