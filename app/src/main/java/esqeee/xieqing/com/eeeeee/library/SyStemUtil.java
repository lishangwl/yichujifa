package esqeee.xieqing.com.eeeeee.library;

import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.widget.Toast;

import com.xieqing.codeutils.util.LogUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.Utils;


import java.lang.reflect.Method;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.receiver.ScreenOffAdminReceiver;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

public class SyStemUtil {
    private static Intent mIntent;

    public static void controlWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }
    }

    public static void screenOff(){
        DevicePolicyManager policyManager = (DevicePolicyManager)Utils.getApp().getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminReceiver = new ComponentName(Utils.getApp(), ScreenOffAdminReceiver.class);
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
            intent.putExtra("android.app.extra.DEVICE_ADMIN", adminReceiver);
            intent.putExtra("android.app.extra.ADD_EXPLANATION", R.string.app_name);
            startActivity(intent);
        }
    }

    public static void controlWifi(Context context, boolean value) {
        ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(value);
    }

    public static void controlBlueTooth(Context context) {
        BluetoothAdapter adapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (adapter == null) {
            return;
        }
        if (adapter.isEnabled()) {
            adapter.disable();
        } else {
            adapter.enable();
        }
    }

    public static void controlBlueTooth(Context context, boolean value) {
        BluetoothAdapter adapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (adapter == null) {
            return;
        }
        if (value) {
            adapter.enable();
        } else {
            adapter.disable();
        }
    }

    public static void controlFlow(Context context) {
        boolean z = true;
        boolean mobile = isMobile(context);
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = mConnectivityManager.getClass().getMethod("setMobileDataEnabled", new Class[]{Boolean.TYPE});
            Object[] objArr = new Object[1];
            if (mobile) {
                z = false;
            }
            objArr[0] = Boolean.valueOf(z);
            method.invoke(mConnectivityManager, objArr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isMobile(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || networkInfo.getType() != 0) {
            return false;
        }
        return true;
    }

    public static void wifiSeting(Context context) {
        mIntent = new Intent("android.settings.WIFI_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
    }

    public static void closeNotification(Context context) {
        try {
            Method collapse;
            Object statusBarManager = context.getSystemService("statusbar");
            if (VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse", new Class[0]);
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels", new Class[0]);
            }
            collapse.invoke(statusBarManager, new Object[0]);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public static void expandNotification(Context context) {
        Object service = context.getSystemService("statusbar");
        if (service != null) {
            try {
                Method expand;
                Class<?> clazz = Class.forName("android.app.StatusBarManager");
                if (VERSION.SDK_INT <= 16) {
                    expand = clazz.getMethod("expand", new Class[0]);
                } else {
                    expand = clazz.getMethod("expandSettingsPanel", new Class[0]);
                }
                expand.setAccessible(true);
                expand.invoke(service, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void memory(Context context) {
        mIntent = new Intent("android.settings.MEMORY_CARD_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        com.xieqing.codeutils.util.Utils.getApp().startActivity(mIntent);
    }

    public static void appList(Context context) {
        mIntent = new Intent("android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        com.xieqing.codeutils.util.Utils.getApp().startActivity(mIntent);
    }

    public static void setting(Context context) {
        mIntent = new Intent("android.settings.SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        com.xieqing.codeutils.util.Utils.getApp().startActivity(mIntent);
    }

    public static void fly(Context context) {
        mIntent = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        com.xieqing.codeutils.util.Utils.getApp().startActivity(mIntent);
    }

    public static void developer(Context context) {
        mIntent = new Intent("android.settings.APPLICATION_DEVELOPMENT_SETTINGS");
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        com.xieqing.codeutils.util.Utils.getApp().startActivity(mIntent);
    }

    public static void ringAndVibrate(Context context) {

        try {
            AudioManager audio = (AudioManager) context.getSystemService("audio");
            audio.setRingerMode(2);
            audio.setVibrateSetting(0, 1);
            audio.setVibrateSetting(1, 1);
        }catch (SecurityException e){
            RuntimeLog.e(e.toString());
        }
    }

    public static void vibrate(Context context) {
        try {
            AudioManager audio = (AudioManager) context.getSystemService("audio");
            audio.setRingerMode(1);
            audio.setVibrateSetting(0, 1);
            audio.setVibrateSetting(1, 1);
        }catch (SecurityException e){
            RuntimeLog.e(e.toString());
        }
    }

    public static void noRingAndVibrate(Context context) {
        try {
            AudioManager audio = (AudioManager) context.getSystemService("audio");
            audio.setRingerMode(0);
            audio.setVibrateSetting(0, 0);
            audio.setVibrateSetting(1, 0);
        }catch (SecurityException e){
            RuntimeLog.e(e.toString());
        }
    }
}